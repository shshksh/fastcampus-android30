package com.fastcampus.ch1youtube

import com.fastcampus.ch1youtube.network.VideoService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl("https://run.mocky.io/")
    .build()

val videoService = retrofit.create(VideoService::class.java)
