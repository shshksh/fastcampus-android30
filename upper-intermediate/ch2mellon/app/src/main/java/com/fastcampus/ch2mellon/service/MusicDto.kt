package com.fastcampus.ch2mellon.service

import com.squareup.moshi.Json

data class MusicDto(
    @Json(name = "musics") val musics: List<MusicEntity>,
)
