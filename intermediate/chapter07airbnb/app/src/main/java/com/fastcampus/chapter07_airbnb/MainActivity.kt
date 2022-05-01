package com.fastcampus.chapter07_airbnb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.fastcampus.chapter07_airbnb.ui.main.MainScreen
import com.fastcampus.chapter07_airbnb.ui.theme.Chapter07airbnbTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

class MainActivity : ComponentActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView = MapView(this).apply {
            id = R.id.map_view
            getMapAsync(this@MainActivity)
        }
        setContent {
            Chapter07airbnbTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    MainScreen(mapView)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: NaverMap) {
        map.maxZoom = 18.0
        map.minZoom = 10.0

        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.497885, 127.027512))

        map.moveCamera(cameraUpdate)

        val uiSetting = map.uiSettings
        uiSetting.isLocationButtonEnabled = true

        map.locationSource = FusedLocationSource(this, 1000)
    }
}