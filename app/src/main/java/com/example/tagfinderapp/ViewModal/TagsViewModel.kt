package com.example.tagfinderapp.ViewModal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagfinderapp.Model.VideoModel
import com.example.tagfinderapp.Network.ApiHandler
import com.example.tagfinderapp.Repository.Repository
import kotlinx.coroutines.launch

class TagsViewModel(private val repository: Repository) : ViewModel() {

    private val _tags = MutableLiveData<ApiHandler<VideoModel>>()
    val tags: LiveData<ApiHandler<VideoModel>> get() = _tags

    fun getData(part: String, id: String, key: String) {
        viewModelScope.launch {
           _tags.value = ApiHandler.Loading()
            val result = repository.getVideotags(part, id, key)
            _tags.value = result

        }
    }
}
