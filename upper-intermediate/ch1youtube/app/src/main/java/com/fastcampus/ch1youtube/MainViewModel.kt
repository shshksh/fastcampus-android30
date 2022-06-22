package com.fastcampus.ch1youtube

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastcampus.ch1youtube.data.VideoModel
import com.fastcampus.ch1youtube.network.VideoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _videoList = MutableStateFlow<List<VideoModel>>(emptyList())
    val videoList: StateFlow<List<VideoModel>> = _videoList

    init {
        viewModelScope.launch {
            _videoList.value = videoService.fetchVideoList().videos
                .map(VideoResponse::toVideoModel)
        }
    }
}
