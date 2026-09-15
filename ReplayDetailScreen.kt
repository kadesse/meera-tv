package com.meera.tv.ui.screens.replays

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meera.tv.data.model.Replay
import com.meera.tv.data.repository.MeeraRepository
import com.meera.tv.player.YouTubePlayer

@Composable
fun ReplayDetailScreen(videoId: String, repository: MeeraRepository = MeeraRepository()) {
    var replay by remember { mutableStateOf<Replay?>(null) }
    val context = LocalContext.current

    LaunchedEffect(videoId) {
        runCatching { repository.getReplayById(videoId) }.onSuccess { replay = it }
    }

    val current = replay
    if (current == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        YouTubePlayer(videoId = current.youtubeVideoId)
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(current.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                IconButton(onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, current.shareUrl())
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Partager"))
                }) {
                    Icon(Icons.Filled.Share, contentDescription = "Partager")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(current.date, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))
            Text(current.description)
        }
    }
}
