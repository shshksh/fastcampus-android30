package com.fastcampus.bookreview.model

import com.google.gson.annotations.SerializedName

data class SearchBookDto(
    @SerializedName("title") val title: String,
    @SerializedName("item") val bookList: List<Book>
)
