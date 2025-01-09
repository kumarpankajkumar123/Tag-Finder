package com.example.tagfinderapp.Model

data class Item(
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet
)