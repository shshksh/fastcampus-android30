package com.fastcampus.ch2mellon.service

import com.squareup.moshi.Json

data class MusicEntity(
    @Json(name = "track") val track: String,
    @Json(name = "streamUrl") val streamUrl: String,
    @Json(name = "artist") val artist: String,
    @Json(name = "coverUrl") val coverUrl: String,
)
