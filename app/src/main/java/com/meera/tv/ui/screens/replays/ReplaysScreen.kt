package com.meera.tv.ui.screens.replays

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

private val categoryLabels = mapOf(
    VideoCategory.CULTES to "Cultes",
    VideoCategory.PREDICATIONS to "Prédications",
    VideoCategory.PRIERES to "Prières",
    VideoCategory.ENSEIGNEMENTS to "Enseignements",
    VideoCategory.TEMOIGNAGES to "Témoignages",
    VideoCategory.EVANGELISATION to "Évangélisation",
    VideoCategory.EMISSIONS to "Émissions MEERA TV"
)

@Composable
fun ReplaysScreen(navController: NavHostController, repository: MeeraRepository = MeeraRepository()) {
    var selectedCategory by remember { mutableStateOf<VideoCategory?>(null) }
    var replays by remember { mutableStateOf<List<Replay>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(selectedCategory) {
        loading = true
        runCatching { repository.getReplays(category = selectedCategory?.name) }
            .onSuccess { replays = it }
        loading = false
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Replays", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(selected = selectedCategory == null, onClick = { selectedCategory = null }, label = { Text("Tout") })
            }
            items(VideoCategory.entries) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(categoryLabels[cat] ?: cat.name) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        if (loading) {
            Box(Modifier.fillMaxWidth().padding(24.dp)) { CircularProgressIndicator() }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(replays) { replay ->
                    ReplayRow(replay) { navController.navigate("replay_detail/${replay.id}") }
                }
            }
        }
    }
}

@Composable
private fun ReplayRow(replay: Replay, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        AsyncImage(
            model = replay.thumbnail(),
            contentDescription = replay.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(width = 120.dp, height = 72.dp).clip(RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(replay.title, fontWeight = FontWeight.Bold, maxLines = 2)
            Text(replay.date, style = MaterialTheme.typography.bodySmall)
        }
    }
}
