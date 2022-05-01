package com.fastcampus.chapter07_airbnb.network.house

import retrofit2.http.GET

interface HouseService {
    @GET("/v3/14d1b967-5cdc-4664-a6e8-8ea5ef5bfc75")
    suspend fun getHouseList(): HouseDto
}