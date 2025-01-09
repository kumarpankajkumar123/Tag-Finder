package com.example.tagfinderapp.Model

data class SnippetX(
    val categoryId: String,
    val channelId: String,
    val channelTitle: String,
    val defaultAudioLanguage: String,
    val defaultLanguage: String,
    val description: String,
    val liveBroadcastContent: String,
    val localized: LocalizedX,
    val publishedAt: String,
    val tags: List<String>,
    val thumbnails: ThumbnailsX,
    val title: String
)