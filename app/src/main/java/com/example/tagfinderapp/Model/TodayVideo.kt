package com.example.tagfinderapp.Model

data class TodayVideo(
    val etag: String,
    val items: List<ItemX>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfoX
)