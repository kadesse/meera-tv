package com.meera.tv.notification

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.meera.tv.MainActivity
import com.meera.tv.MeeraTvApp
import com.meera.tv.R

/**
 * Reçoit les notifications envoyées gratuitement depuis la console Firebase
 * (Engage > Messaging > Nouvelle campagne, en ciblant le topic "meera_all")
 * — aucun serveur à nous n'est nécessaire pour envoyer une notification.
 */
class MeeraMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: message.data["title"] ?: "J-C TV"
        val body = message.notification?.body ?: message.data["body"] ?: ""
        val type = message.data["type"] ?: "ANNOUNCEMENT"

        val channelId = if (type == "LIVE_STARTED") MeeraTvApp.CHANNEL_LIVE else MeeraTvApp.CHANNEL_GENERAL

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), notification)
    }
}

