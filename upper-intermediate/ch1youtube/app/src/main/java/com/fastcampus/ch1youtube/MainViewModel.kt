package com.fastcampus.ch1youtube

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastcampus.ch1youtube.data.VideoModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _videoList = MutableStateFlow<List<VideoModel>>(emptyList())
    val videoList: StateFlow<List<VideoModel>> = _videoList

    private val _videoClickEvent = MutableSharedFlow<VideoModel>()
    val videoClickEvent: SharedFlow<VideoModel> = _videoClickEvent

    init {
        viewModelScope.launch {
            _videoList.value = videoService.fetchVideoList().videos
                .map { it.toVideoModel(_videoClickEvent::tryEmit) }
        }
    }
}
