package com.example.heung

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class RentActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var gMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.googlemap_icon)
        title = "시흥시 연습실 대여점"

        val mapFragment = fragmentManager.findFragmentById(R.id.map) as? MapFragment
        mapFragment?.getMapAsync(this)

        initBottomNavigation()
    }
    private fun initBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_main -> {
                    // 현재 액티비티가 이미 MainActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == MainActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 메인화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_recruit -> {
                    // 현재 액티비티가 이미 RecruListActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == RecruListActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RecruListActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 모집 화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_map -> {
                    // 현재 액티비티가 이미 RentActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == RentActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, RentActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 지도 화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_calendar -> {
                    // 현재 액티비티가 이미 CalActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == CalActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, CalActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 달력/일정 화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    // 현재 액티비티가 이미 SelfProfActivity인 경우, 액티비티를 전환하지 않습니다.
                    if (javaClass.name == SelfProfActivity::class.java.name) {
                        return@setOnNavigationItemSelectedListener true
                    }
                    val intent = Intent(this, SelfProfActivity::class.java)
                    startActivity(intent)
                    finish() // 현재 액티비티를 종료하여 뒤로 가기 시 프로필 화면으로 돌아가도록 합니다.
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
        bottomNavigationView.menu.findItem(R.id.nav_map)?.isChecked = true // 하단바 상태 유지
    }
    override fun onMapReady(map: GoogleMap) {
        gMap = map
        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.setOnMapClickListener { point ->
            val videoMark = GroundOverlayOptions().image(
                BitmapDescriptorFactory.fromResource(R.drawable.googlemap_marker))
                .position(point, 100f, 100f)
            gMap.addGroundOverlay(videoMark)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 1, 0, "문MS 음악연습실")
        menu.add(0, 2, 0, "한스헨즈음악")
        menu.add(0, 3, 0, "릴라이언 뮤직스튜디오")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val location: LatLng
        val title: String
        when (item.itemId) {
            1 -> {
                location = LatLng(37.340794, 126.733745)
                title = "문MS 음악연습실"
            }
            2 -> {
                location = LatLng(37.341719,  126.735198)
                title = "한스헨즈음악"
            }
            3 -> {
                location = LatLng(37.378046, 126.729134)
                title = "릴라이언 뮤직스튜디오"
            }
            else -> return false
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        gMap.clear()
        gMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(title)
        )
        return true
    }
}