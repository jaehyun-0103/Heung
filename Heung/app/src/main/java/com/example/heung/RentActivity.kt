package com.example.heung

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