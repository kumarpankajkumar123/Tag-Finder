package com.example.tagfinderapp.Model

data class VideoModel(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val pageInfo: PageInfo
)