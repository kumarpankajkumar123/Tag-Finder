package com.example.tagfinderapp.ViewModal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagfinderapp.Model.TodayVideo
import com.example.tagfinderapp.Network.ApiHandler
import com.example.tagfinderapp.Repository.Repository
import kotlinx.coroutines.launch

class VideoViewModel(private val repository : Repository) : ViewModel() {

    private val todayVideo = MutableLiveData<ApiHandler<TodayVideo>>()
    val _todayVideo : LiveData<ApiHandler<TodayVideo>>
                get() = todayVideo

    fun getTodayVideo(part : String, chart : String, key : String,code: String,maxResult : Int) {
      viewModelScope.launch {
          todayVideo.value = ApiHandler.Loading()
          val result = repository.getTodayVideo(part, chart, key, code, maxResult)
          todayVideo.value = result
      }

    }
}