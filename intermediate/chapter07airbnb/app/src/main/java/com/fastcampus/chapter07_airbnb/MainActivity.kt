package com.fastcampus.chapter07_airbnb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fastcampus.chapter07_airbnb.ui.main.MainScreen
import com.fastcampus.chapter07_airbnb.ui.theme.Chapter07airbnbTheme
import com.naver.maps.map.MapView

class MainActivity : ComponentActivity() {

    private val mapView: MapView? by lazy { findViewById(R.id.map_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chapter07airbnbTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Chapter07airbnbTheme {
        MainScreen()
    }
}