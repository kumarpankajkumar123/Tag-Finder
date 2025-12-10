package com.example.tagfinderapp.Model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageInfoXX(
    @SerialName("resultsPerPage")
    val resultsPerPage: Int?,
    @SerialName("totalResults")
    val totalResults: Int?
)