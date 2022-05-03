package com.fastcampus.chapter07_airbnb.ui.main

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.fastcampus.chapter07_airbnb.data.HouseModel
import com.fastcampus.chapter07_airbnb.ui.theme.Chapter07airbnbTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.naver.maps.map.MapView
import com.skydoves.landscapist.glide.GlideImage


@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(mapView: MapView) {
    val scaffoldState = rememberBottomSheetScaffoldState()

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
    ) { innerPadding ->
        NaverMapView(mapView = mapView)

        HousePager(modifier = Modifier.padding(innerPadding))
    }
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

@ExperimentalPagerApi
@Composable
private fun HousePager(modifier: Modifier = Modifier) {
    HorizontalPager(
        count = 10,
        modifier = modifier
            .fillMaxSize()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
    }
}

@Composable
fun HouseCard(houseModel: HouseModel) {
    Card(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            GlideImage(
                imageModel = houseModel.imgUrl,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(
                    text = houseModel.title,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 12.dp),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = houseModel.price,
                    style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 100)
@Composable
fun HouseCardPreview() {
    val sampleModel = HouseModel(
        id = 0,
        title = "미금역 오피스텔",
        price = "5000/80",
        imgUrl = "",
        lat = 37.0,
        lng = 127.0
    )

    Chapter07airbnbTheme {
        HouseCard(sampleModel)
    }
}