package com.fastcampus.chapter07_airbnb.data

import com.squareup.moshi.Json

data class HouseModel(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "price") val price: String,
    @field:Json(name = "imgUrl") val imgUrl: String,
    @field:Json(name = "lat") val lat: Double,
    @field:Json(name = "lng") val lng: Double,
)
