package com.example.tagfinderapp.Model

data class SearchResult(
    val query: String,
    val suggestions: List<String>,
    val suggestSubtypes: List<List<Int>>
)
