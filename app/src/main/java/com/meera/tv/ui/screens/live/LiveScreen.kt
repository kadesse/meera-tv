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

@Composable
fun LiveScreen(repository: MeeraRepository = MeeraRepository()) {
    var status by remember { mutableStateOf(LiveStatus()) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        while (true) {
            try {
                val result = repository.getLiveStatus()
                status = result
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = e.message ?: e.javaClass.simpleName
            } finally {
                loading = false
            }

            delay(20_000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Direct J-C TV",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        when {
            loading -> {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Erreur de connexion Firestore",
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(errorMessage ?: "Erreur inconnue")
                    }
                }
            }

            status.isLive && !status.youtubeVideoId.isNullOrBlank() -> {
                YouTubePlayer(
                    videoId = status.youtubeVideoId!!
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    status.title ?: "En direct",
                    fontWeight = FontWeight.Bold
                )
            }

            else -> {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Aucun direct en ce moment",
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "Consultez l'onglet Programmes pour connaître les prochains horaires de diffusion."
                        )
                    }
                }
            }
        }
    }
}