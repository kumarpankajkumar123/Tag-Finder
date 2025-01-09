package com.example.tagfinderapp.ViewModal

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tagfinderapp.Model.SearchResult
import com.example.tagfinderapp.Repository.Repository
import kotlinx.coroutines.launch

class KeyworsViewModel(private val repository : Repository) : ViewModel() {

    private val _data = MutableLiveData<Result<List<Any>>>()
    val data: LiveData<Result<List<Any>>> = _data

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getKeywords(key: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val result = repository.generateKeywords(key)
                _data.postValue(result)
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}