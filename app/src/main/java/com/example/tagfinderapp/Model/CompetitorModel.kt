package com.example.tagfinderapp.Model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompetitorModel(
    @SerialName("etag")
    val etag: String?,
    @SerialName("items")
    val items: List<ItemXX>?,
    @SerialName("kind")
    val kind: String?,
    @SerialName("nextPageToken")
    val nextPageToken: String?,
    @SerialName("pageInfo")
    val pageInfo: PageInfoXX?,
    @SerialName("regionCode")
    val regionCode: String?
)