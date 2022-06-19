package com.fastcampus.ch1youtube.network

import retrofit2.http.GET

interface VideoService {

    @GET("/v3/9b1bef58-4ec2-46be-9a35-04dfaf62b0f7")
    suspend fun fetchVideoList(): VideoListResponse
}
