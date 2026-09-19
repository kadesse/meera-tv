package com.meera.tv.data.model

/**
 * Statut du direct MEERA TV, stocké dans Firestore (collection "config",
 * document "liveStatus") et mis à jour manuellement depuis l'admin quand
 * un direct YouTube commence/se termine.
 */

enum class VideoCategory {
    CULTES, PREDICATIONS, PRIERES, ENSEIGNEMENTS, TEMOIGNAGES, EVANGELISATION, EMISSIONS
}

data class Replay(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val date: String = "",
    val youtubeVideoId: String = "",
    val thumbnailUrl: String = "" // dérivée automatiquement de l'ID YouTube si vide
) {
    fun thumbnail(): String =
        thumbnailUrl.ifBlank { "https://img.youtube.com/vi/$youtubeVideoId/hqdefault.jpg" }

    fun shareUrl(): String = "https://youtu.be/$youtubeVideoId"
}

data class Program(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val category: String = "",
    val startTime: String = "", // ISO 8601
    val endTime: String = ""    // ISO 8601
)

data class Announcement(
    val id: String = "",
    val title: String = "",
    val body: String = "",
    val imageUrl: String = "",
    val createdAt: String = ""
)

data class PrayerRequest(
    val name: String = "",
    val phoneOrEmail: String? = null,
    val message: String = "",
    val createdAt: String = ""
)
