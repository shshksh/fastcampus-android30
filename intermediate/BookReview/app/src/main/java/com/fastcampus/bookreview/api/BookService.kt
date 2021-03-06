package com.fastcampus.bookreview.api

import com.fastcampus.bookreview.model.BestSellerDto
import com.fastcampus.bookreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val retrofit: Retrofit by lazy {
    Retrofit.Builder()
        .baseUrl("https://book.interpark.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

val bookService by lazy { createBookService() }

interface BookService {

    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(@Query("key") apiKey: String): Call<BestSellerDto>
}

private fun createBookService(): BookService {
    return retrofit.create(BookService::class.java)
}