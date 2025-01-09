package com.example.tagfinderapp.ViewModal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagfinderapp.Model.TodayVideo
import com.example.tagfinderapp.Repository.Repository
import kotlinx.coroutines.launch

class VideoViewModel(private val repository : Repository) : ViewModel() {

    private val todayVideo = MutableLiveData<Result<TodayVideo>>()
    val _todayVideo : LiveData<Result<TodayVideo>>
                get() = todayVideo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getTodayVideo(part : String, chart : String, key : String,code: String,maxResult : Int) {
      viewModelScope.launch {
          _isLoading.postValue(true) // Start loading
          try {
              val result = repository.getTodayVideo(part, chart, key, code, maxResult)
              todayVideo.postValue(result)
          } catch (e: Exception) {
              todayVideo.postValue(Result.failure(e)) // Handle failure
          } finally {
              _isLoading.postValue(false) // Stop loading
          }
      }

    }
}