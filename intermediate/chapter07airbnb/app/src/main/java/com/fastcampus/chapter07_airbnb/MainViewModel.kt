package com.fastcampus.chapter07_airbnb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fastcampus.chapter07_airbnb.data.HouseModel
import com.fastcampus.chapter07_airbnb.network.house.HouseService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainViewModel : ViewModel() {

    private val houseService: HouseService

    private val _houseList: MutableStateFlow<List<HouseModel>> = MutableStateFlow(listOf())
    val houseList: StateFlow<List<HouseModel>> = _houseList

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        houseService = retrofit.create(HouseService::class.java)

        fetchHouseList()
    }

    fun fetchHouseList() {
        viewModelScope.launch {
            _houseList.value = houseService.getHouseList().items
        }
    }

    fun updatePosition(position: Int) {
        _currentPosition.value = position
    }
}