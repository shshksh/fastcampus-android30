package com.fastcampus.chapter07_airbnb.ui.main

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.naver.maps.map.MapView


@ExperimentalMaterialApi
@Composable
fun MainScreen(mapView: MapView) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    NaverMapView(mapView = mapView)
    BottomSheetScaffold(
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "여러개의 숙소",
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Cyan),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 100.dp,
        sheetShape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(30.dp),
            topEnd = CornerSize(30.dp)
        ),
    ) {}
}

@Composable
fun NaverMapView(mapView: MapView, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize()
    )

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(key1 = lifecycle, key2 = mapView) {
        val lifecycleObserver = getMapViewLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

private fun getMapViewLifecycleObserver(mapView: MapView) = LifecycleEventObserver { _, event ->
    when (event) {
        Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
        Lifecycle.Event.ON_START -> mapView.onStart()
        Lifecycle.Event.ON_RESUME -> mapView.onResume()
        Lifecycle.Event.ON_PAUSE -> mapView.onPause()
        Lifecycle.Event.ON_STOP -> mapView.onStop()
        Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
        else -> throw  IllegalStateException()
    }
}