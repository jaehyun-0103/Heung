package com.example.heung

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import data.Store

class RentActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var locationNameTextView: TextView
    private lateinit var locationAddressTextView: TextView
    private lateinit var locationPhoneTextView: TextView
    private lateinit var locationContentTextView: TextView
    private lateinit var stores: List<Store>
    private var markerList: MutableList<Marker> = mutableListOf()
    private lateinit var spinnerCategory: Spinner

    private val autoCompleteOptions = arrayOf(
        "대야동", "신천동", "신현동", "은행동", "매화동",
        "목감동", "군자동", "월곶동", "정왕본동", "정왕1동",
        "정왕2동", "정왕3동", "정왕4동", "배곧1동", "배곧2동",
        "과림동", "연성동", "장곡동", "능곡동"
    )

    private lateinit var locationManager: LocationManager
    private val DEFAULT_ZOOM = 15f
    private val MIN_TIME_BETWEEN_UPDATES = 1000L // 1초
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10f // 10미터
    private val REQUEST_LOCATION_PERMISSION = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        spinnerCategory = findViewById(R.id.dropdown)

        initBottomNavigation()

        val searchBar: AutoCompleteTextView =
            findViewById<AutoCompleteTextView>(R.id.searchBar)
        val autoCompleteAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            autoCompleteOptions
        )
        searchBar.setAdapter(autoCompleteAdapter)

        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            val district = searchBar.text.toString()
            if (district.isNotBlank()) {
                getStoresByDistrictFromFirebase(district)
            }
            spinnerCategory.setSelection(0)
        }

        val locationNowButton = findViewById<Button>(R.id.locationNow)
        locationNowButton.setOnClickListener {
            getCurrentLocation()
        }

        // TextView 초기화
        locationNameTextView = findViewById(R.id.locationNameTextView)
        locationAddressTextView = findViewById(R.id.locationAddressTextView)
        locationPhoneTextView = findViewById(R.id.locationPhoneTextView)
        locationContentTextView = findViewById(R.id.locationContentTextView)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()

        // 위치 권한 확인 및 요청
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        stopLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // 지도 초기화 설정
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        googleMap.setOnMarkerClickListener { marker ->
            // 마커 클릭 시 가게 정보 표시
            showStoreInfo(marker)
            true
        }
    }

    private fun initBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    if (javaClass.name == MainActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_recruit -> {
                    if (javaClass.name == RecruListActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RecruListActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_map -> {
                    if (javaClass.name == RentActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RentActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_calendar -> {
                    if (javaClass.name == CalActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, CalActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    if (javaClass.name == SelfProfActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, SelfProfActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
        bottomNavigationView.menu.findItem(R.id.nav_map)?.isChecked = true
    }

    private fun showStoreInfo(marker: Marker) {
        // 선택한 마커의 가게 정보 가져오기
        val selectedStore = stores.find { it.name == marker.title }

        if (selectedStore != null) {
            // 가게 정보를 이용하여 하단에 표시
            locationNameTextView.text = selectedStore.name
            locationAddressTextView.text = selectedStore.address
            locationPhoneTextView.text = selectedStore.phoneNumber
            locationContentTextView.text = selectedStore.content
        }
    }

    private fun getStoresByDistrictFromFirebase(district: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Store")
            .whereEqualTo("district", district.toLowerCase())
            .get()
            .addOnSuccessListener { result ->
                val storeList = mutableListOf<Store>()
                for (document in result) {
                    val name = document.getString("name")
                    val address = document.getString("address")
                    val location = document.getGeoPoint("location")
                    val phoneNumber = document.getString("phoneNumber")
                    val content = document.getString("content")

                    if (name != null && address != null && location != null && phoneNumber != null && content != null) {
                        val store = Store(name, address, location, district, phoneNumber, content)
                        storeList.add(store)
                    }
                }
                val districtLatLng = getDistrictLatLng(district)
                if (districtLatLng != null) {
                    googleMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            districtLatLng,
                            DEFAULT_ZOOM
                        )
                    )
                }
                stores = storeList
                addMarkersToMap()
            }
            .addOnFailureListener { exception ->
                // 가져오기 실패
            }
    }

    private fun addMarkersToMap() {
        // 기존의 마커 제거
        for (marker in markerList) {
            marker.remove()
        }
        markerList.clear()

        // 가게 정보를 기반으로 마커를 추가
        for (store in stores) {
            val location = LatLng(store.location.latitude, store.location.longitude)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(store.name)
            )
            if (marker != null) {
                markerList.add(marker)
            }
        }

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parent.getItemAtPosition(position) as String

                // 선택된 카테고리에 따라 원하는 동작을 수행
                when (selectedCategory) {
                    "전체" -> {
                        Toast.makeText(this@RentActivity, "전체를 선택했습니다.", Toast.LENGTH_SHORT).show()
                        // 기존의 마커 제거
                        for (marker in markerList) {
                            marker.remove()
                        }
                        markerList.clear()

                        // 가게 정보를 기반으로 마커를 추가
                        for (store in stores) {
                            val location = LatLng(store.location.latitude, store.location.longitude)
                            val marker = googleMap.addMarker(
                                MarkerOptions()
                                    .position(location)
                                    .title(store.name)
                            )
                            if (marker != null) {
                                markerList.add(marker)
                            }
                        }
                    }
                    "공연장" -> {
                        Toast.makeText(this@RentActivity, "공연장을 선택했습니다.", Toast.LENGTH_SHORT).show()
                        // 기존의 마커 제거
                        for (marker in markerList) {
                            marker.remove()
                        }
                        markerList.clear()

                        for (store in stores) {
                            if (store.content == "공연장") {
                                val location =
                                    LatLng(store.location.latitude, store.location.longitude)
                                val marker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(location)
                                        .title(store.name)
                                )
                                if (marker != null) {
                                    markerList.add(marker)
                                }
                            }
                        }
                    }
                    "대여점" -> {
                        Toast.makeText(this@RentActivity, "대여점을 선택했습니다.", Toast.LENGTH_SHORT).show()
                        // 기존의 마커 제거
                        for (marker in markerList) {
                            marker.remove()
                        }
                        markerList.clear()

                        for (store in stores) {
                            if (store.content == "대여점") {
                                val location =
                                    LatLng(store.location.latitude, store.location.longitude)
                                val marker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(location)
                                        .title(store.name)
                                )
                                if (marker != null) {
                                    markerList.add(marker)
                                }
                            }
                        }
                    }
                    "강의실" -> {
                        Toast.makeText(this@RentActivity, "강의실을 선택했습니다.", Toast.LENGTH_SHORT).show()
                        // 기존의 마커 제거
                        for (marker in markerList) {
                            marker.remove()
                        }
                        markerList.clear()

                        for (store in stores) {
                            if (store.content == "강의실") {
                                val location =
                                    LatLng(store.location.latitude, store.location.longitude)
                                val marker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(location)
                                        .title(store.name)
                                )
                                if (marker != null) {
                                    markerList.add(marker)
                                }
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 아무것도 선택하지 않은 경우의 동작 처리
            }
        }
    }

    private fun startLocationUpdates() {
        // 위치 업데이트 시작
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                this
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                this
            )
        }
    }

    private fun stopLocationUpdates() {
        // 위치 업데이트 중지
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        // 위치 변경 시 호출되는 콜백 메서드
        val currentLatLng = LatLng(location.latitude, location.longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM))

        // 위치 업데이트가 더 이상 필요하지 않은 경우 중지
        stopLocationUpdates()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // 상태 변경 시 호출되는 콜백 메서드
    }

    override fun onProviderEnabled(provider: String) {
        // 제공자 사용 가능 상태로 변경 시 호출되는 콜백 메서드
    }

    override fun onProviderDisabled(provider: String) {
        // 제공자 사용 불가능 상태로 변경 시 호출되는 콜백 메서드
    }

    private fun getDistrictLatLng(district: String): LatLng? {
        return when (district) {
            "대야동" -> LatLng(37.45298041145947, 126.79970379484628)
            "신천동" -> LatLng(37.43855076661446, 126.7800307699699)
            "신현동" -> LatLng(37.4150902869363, 126.77425756273097)
            "은행동" -> LatLng(37.432393890628674, 126.80785454699657)
            "매화동" -> LatLng(37.4155123278641, 126.81135006744307)
            "목감동" -> LatLng(37.38192688571677, 126.85163199890775)
            "군자동" -> LatLng(37.35622282716196, 126.77853898276557)
            "월곶동" -> LatLng(37.38340425780903, 126.75800987830766)
            "정왕본동" -> LatLng(37.357849139779155, 126.75119538493419)
            "정왕1동" -> LatLng(37.33148300048798, 126.73506811432705)
            "정왕2동" -> LatLng(37.332541149162, 126.70985005160574)
            "정왕3동" -> LatLng(37.34494709914915, 126.70478090521371)
            "정왕4동" -> LatLng(37.36244435201556, 126.73112362103159)
            "배곧1동" -> LatLng(37.37602552754929, 126.73033539324881)
            "배곧2동" -> LatLng(37.362467560167836, 126.71753999254182)
            "과림동" -> LatLng(37.44199654823024, 126.83183277233348)
            "연성동" -> LatLng(37.38864438096801, 126.8057347466128)
            "장곡동" -> LatLng(37.382121939856965, 126.78384323278573)
            "능곡동" -> LatLng(37.36529913085225, 126.80895559598629)
            else -> null
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null)
        }
    }
}