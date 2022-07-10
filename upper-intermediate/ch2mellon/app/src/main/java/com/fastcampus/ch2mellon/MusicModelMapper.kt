package com.fastcampus.ch2mellon

import com.fastcampus.ch2mellon.service.MusicEntity

fun MusicEntity.mapper(id: Long): MusicModel = MusicModel(
    id = id,
    track = track,
    streamUrl = streamUrl,
    artist = artist,
    coverUrl = coverUrl
)
