package com.meera.tv.ui.screens.live

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meera.tv.data.model.LiveStatus
import com.meera.tv.data.repository.MeeraRepository
import com.meera.tv.player.YouTubePlayer
import kotlinx.coroutines.delay

/**
 * Écran "Direct". Le statut (isLive + ID de la vidéo YouTube) est mis à jour
 * manuellement par l'admin quand un direct commence sur la chaîne YouTube
 * J-C TV — on relit Firestore toutes les 20s pour rester à jour.
 */
@Composable
fun LiveScreen(repository: MeeraRepository = MeeraRepository()) {
    var status by remember { mutableStateOf(LiveStatus()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            runCatching { repository.getLiveStatus() }.onSuccess {
                status = it
                loading = false
            }
            delay(20_000)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Direct J-C TV", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        when {
            loading -> Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            status.isLive && !status.youtubeVideoId.isNullOrBlank() -> {
                YouTubePlayer(videoId = status.youtubeVideoId!!)
                Spacer(Modifier.height(12.dp))
                Text(status.title ?: "En direct", fontWeight = FontWeight.Bold)
            }
            else -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Aucun direct en ce moment", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Text("Consultez l'onglet Programmes pour connaître les prochains horaires de diffusion.")
                    }
                }
            }
        }
    }
}

