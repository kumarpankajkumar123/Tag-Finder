package com.example.tagfinderapp.ViewModal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagfinderapp.Repository.Repository

class VideoViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoViewModel::class.java)) {
            return VideoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
