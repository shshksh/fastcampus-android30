package com.fastcampus.chapter07_airbnb.network.house

import com.fastcampus.chapter07_airbnb.data.HouseModel
import com.squareup.moshi.Json

data class HouseDto(
    @field:Json(name = "items") val items: List<HouseModel>,
)
