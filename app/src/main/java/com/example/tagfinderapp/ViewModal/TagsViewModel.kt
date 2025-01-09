package com.example.tagfinderapp.ViewModal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.Repository.Repository
import kotlinx.coroutines.launch

class TagsViewModel(private val repository: Repository) : ViewModel() {

    private val _tags = MutableLiveData<Result<VideoModel>>()
    val tags: LiveData<Result<VideoModel>> get() = _tags

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getData(part: String, id: String, key: String) {
        viewModelScope.launch {
            _isLoading.postValue(true) // Start loading
            try {
                val result = repository.getVideotags(part, id, key)
                _tags.postValue(result)
            } catch (e: Exception) {
                _tags.postValue(Result.failure(e)) // Post error if any
            } finally {
                _isLoading.postValue(false) // Stop loading
            }
        }
    }
}
