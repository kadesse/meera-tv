package com.meera.tv.ui.screens.sermons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.meera.tv.data.model.Replay
import com.meera.tv.data.model.VideoCategory
import com.meera.tv.data.repository.MeeraRepository

/**
 * L'onglet "Prédications" du menu principal correspond à la catégorie de
 * replays PREDICATIONS. Les vidéos elles-mêmes sont gérées depuis l'admin
 * (même contenu, même structure que les autres catégories de replays).
 */
@Composable
fun SermonsScreen(navController: NavHostController, repository: MeeraRepository = MeeraRepository()) {
    var sermons by remember { mutableStateOf<List<Replay>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        runCatching { repository.getReplays(category = VideoCategory.PREDICATIONS.name) }
            .onSuccess { sermons = it }
        loading = false
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Prédications", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(sermons) { sermon ->
                    Row(Modifier.fillMaxWidth().clickable {
                        navController.navigate("replay_detail/${sermon.id}")
                    }) {
                        AsyncImage(
                            model = sermon.thumbnail(),
                            contentDescription = sermon.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(width = 120.dp, height = 72.dp).clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(sermon.title, fontWeight = FontWeight.Bold, maxLines = 2)
                            Text(sermon.date, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
