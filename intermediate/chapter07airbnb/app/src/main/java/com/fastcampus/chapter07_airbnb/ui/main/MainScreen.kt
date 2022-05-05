package com.fastcampus.chapter07_airbnb.ui.main

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fastcampus.chapter07_airbnb.MainViewModel
import com.fastcampus.chapter07_airbnb.data.HouseModel
import com.fastcampus.chapter07_airbnb.ui.theme.Chapter07airbnbTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.naver.maps.map.MapView
import com.naver.maps.map.widget.LocationButtonView


@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(
    mapView: MapView,
    locationButton: LocationButtonView,
    viewModel: MainViewModel = viewModel(),
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val houseList by viewModel.houseList.collectAsState()

    BottomSheetScaffold(
        sheetContent = {
            HouseListHeader()
            Divider(color = Color(0xFFCCCCCC), thickness = 1.dp)
            HouseList(houseList)
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 100.dp,
        sheetShape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(30.dp),
            topEnd = CornerSize(30.dp)
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            NaverMapView(mapView = mapView,
                locationButton,
                modifier = Modifier.padding(bottom = 80.dp))
            HousePager(modifier = Modifier.padding(bottom = 120.dp), houseList)
        }
    }
}

@Composable
fun NaverMapView(
    mapView: MapView,
    locationButton: LocationButtonView,
    modifier: Modifier = Modifier,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
        AndroidView(
            factory = { mapView },
            modifier = modifier.fillMaxSize()
        )
        AndroidView(
            factory = { locationButton },
            modifier = Modifier
                .wrapContentSize()
                .padding(12.dp)
        )
    }

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
private fun HousePager(modifier: Modifier = Modifier, houseList: List<HouseModel>) {
    HorizontalPager(
        count = houseList.size,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        HouseCard(houseModel = houseList[page])
    }
}

@Composable
fun HouseCard(houseModel: HouseModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 30.dp, end = 30.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = houseModel.imgUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = houseModel.title,
                        maxLines = 2
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = houseModel.price,
                    style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun HouseListHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(3.dp)
                    .background(
                        color = Color(0xffcccccc),
                        shape = RoundedCornerShape(15.dp)
                    )
            )
        }
        Text(
            text = "여러개의 숙소",
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun HouseList(houseList: List<HouseModel>) {
    LazyColumn {
        items(count = houseList.size) { index ->
            HouseListItem(houseList[index])
        }
    }
}

@Composable
private fun HouseListItem(houseModel: HouseModel) {
    Column(modifier = Modifier.padding(24.dp)) {
        AsyncImage(
            model = houseModel.imgUrl,
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(3f / 2f)
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = houseModel.title,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = houseModel.price,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
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

@Preview(showBackground = true)
@Composable
fun HouseListItemPreview() {
    val sampleModel = HouseModel(
        id = 0,
        title = "미금역 오피스텔",
        price = "5000/80",
        imgUrl = "",
        lat = 37.0,
        lng = 127.0
    )

    Chapter07airbnbTheme {
        HouseListItem(sampleModel)
    }
}