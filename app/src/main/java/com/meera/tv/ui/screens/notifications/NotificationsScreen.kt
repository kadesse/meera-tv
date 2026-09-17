package com.meera.tv.ui.screens.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meera.tv.data.model.Announcement
import com.meera.tv.data.repository.MeeraRepository

/**
 * Affiche les annonces publiées depuis l'admin. Les vraies notifications
 * "push" (reçues même app fermée) sont envoyées gratuitement depuis la
 * console Firebase — cet écran sert d'historique consultable dans l'app.
 */
@Composable
fun NotificationsScreen(repository: MeeraRepository = MeeraRepository()) {
    var announcements by remember { mutableStateOf<List<Announcement>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        runCatching { repository.getAnnouncements() }.onSuccess { announcements = it }
        loading = false
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Notifications", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (announcements.isEmpty()) {
            Text("Vous n'avez pas encore reçu de notification.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(announcements) { ann ->
                    Card {
                        Column(Modifier.padding(12.dp)) {
                            Text(ann.title, fontWeight = FontWeight.Bold)
                            Text(ann.body, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
