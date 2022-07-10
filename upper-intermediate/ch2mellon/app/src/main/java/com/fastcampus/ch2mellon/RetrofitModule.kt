package com.fastcampus.ch2mellon

import com.fastcampus.ch2mellon.service.MusicService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val retrofit = Retrofit.Builder()
    .baseUrl("https://run.mocky.io/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

val musicService = retrofit.create(MusicService::class.java)
