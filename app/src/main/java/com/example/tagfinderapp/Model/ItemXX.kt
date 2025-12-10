package com.example.tagfinderapp.Model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemXX(
    @SerialName("etag")
    val etag: String?,
    @SerialName("id")
    val id: Id?,
    @SerialName("kind")
    val kind: String?,
    @SerialName("snippet")
    val snippet: SnippetXX?
)