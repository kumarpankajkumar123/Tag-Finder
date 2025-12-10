package com.example.tagfinderapp.ViewModal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagfinderapp.Model.CompetitorModel
import com.example.tagfinderapp.Network.ApiHandler
import com.example.tagfinderapp.Repository.Repository
import kotlinx.coroutines.launch

class KeyworsViewModel(private val repository: Repository) : ViewModel() {

    private val _competitorData = MutableLiveData<ApiHandler<CompetitorModel?>>()
    val competitorData: LiveData<ApiHandler<CompetitorModel?>> get() = _competitorData

    fun getCompetitor(part: String, key: String, maxResults: String, type: String, q: String) {
        viewModelScope.launch {
            _competitorData.value = ApiHandler.Loading()
            val response = repository.getCompetitors(part, key, maxResults, q, type)
            _competitorData.value = response

        }
    }
}