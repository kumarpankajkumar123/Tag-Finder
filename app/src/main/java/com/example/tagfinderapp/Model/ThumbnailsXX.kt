package com.example.tagfinderapp.Model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThumbnailsXX(
    @SerialName("default")
    val default: DefaultXX?,
    @SerialName("high")
    val high: HighXX?,
    @SerialName("medium")
    val medium: MediumXX?
)