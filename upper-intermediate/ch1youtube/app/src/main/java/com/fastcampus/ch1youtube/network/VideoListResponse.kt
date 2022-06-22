package com.fastcampus.ch1youtube.network

import com.fastcampus.ch1youtube.data.VideoModel
import com.squareup.moshi.Json


data class VideoListResponse(
    @field:Json(name = "videos") val videos: List<VideoResponse>,
)

data class VideoResponse(
    @field:Json(name = "description") val description: String,
    @field:Json(name = "sources") val sources: String,
    @field:Json(name = "subtitle") val subtitle: String,
    @field:Json(name = "thumb") val thumb: String,
    @field:Json(name = "title") val title: String,
) {
    fun toVideoModel() = VideoModel(
        description = description,
        sources = sources,
        subtitle = subtitle,
        thumb = thumb,
        title = title
    )
}
