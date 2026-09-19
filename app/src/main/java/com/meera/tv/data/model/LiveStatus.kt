package com.meera.tv.data.model

data class LiveStatus(
    val isLive: Boolean = false,
    val title: String? = null,
    val youtubeVideoId: String? = null
)