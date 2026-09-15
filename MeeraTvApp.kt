package com.meera.tv

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging

class MeeraTvApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        // Abonnement gratuit à un "topic" Firebase Cloud Messaging : permet
        // d'envoyer une notification à tous les utilisateurs de l'app sans
        // avoir besoin de gérer soi-même une liste d'appareils/serveur.
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_ALL)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)

            val liveChannel = NotificationChannel(
                CHANNEL_LIVE,
                "Direct MEERA TV",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Alertes quand MEERA TV est en direct" }

            val generalChannel = NotificationChannel(
                CHANNEL_GENERAL,
                "Annonces & Programmes",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Programmes, replays et annonces MEERA TV" }

            manager.createNotificationChannel(liveChannel)
            manager.createNotificationChannel(generalChannel)
        }
    }

    companion object {
        const val CHANNEL_LIVE = "meera_live_channel"
        const val CHANNEL_GENERAL = "meera_general_channel"
        const val TOPIC_ALL = "meera_all"
    }
}
