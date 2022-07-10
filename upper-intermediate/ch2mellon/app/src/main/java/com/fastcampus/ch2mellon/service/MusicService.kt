package com.fastcampus.ch2mellon.service

import retrofit2.http.GET

interface MusicService {
    @GET("v3/1efd8834-883e-4603-95d6-609a5258e0f2")
    suspend fun listMusics(): MusicDto
}
