package com.meera.tv.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.meera.tv.Screen
import com.meera.tv.data.model.Replay
import com.meera.tv.ui.theme.MeeraGold
import com.meera.tv.ui.theme.MeeraLiveRed
import com.meera.tv.R
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item { MeeraHeader(navController) }
        item { Spacer(Modifier.height(16.dp)) }

        item {
            LiveBanner(
                isLive = state.liveStatus.isLive,
                title = state.liveStatus.title,
                onClick = { navController.navigate(Screen.Live.route) }
            )
        }

        item { Spacer(Modifier.height(20.dp)) }

        item {
            Text("Programmes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            ProgramRow(label = "EN COURS", title = state.currentProgram?.title ?: "Aucun programme en cours")
            Spacer(Modifier.height(6.dp))
            ProgramRow(label = "PROCHAIN", title = state.nextProgram?.title ?: "À venir")
        }

        if (state.announcements.isNotEmpty()) {
            item { Spacer(Modifier.height(20.dp)) }
            item {
                Text("Annonces", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
            }
            items(state.announcements) { ann ->
                Text("• ${ann.title}", modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        item { Spacer(Modifier.height(20.dp)) }
        item {
            Text("Derniers replays", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(state.latestReplays) { replay ->
                    ReplayCard(replay) {
                        navController.navigate("replay_detail/${replay.id}")
                    }
                }
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun MeeraHeader(navController: NavHostController) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Emplacement réservé au logo MEERA (voir /app/src/main/res/drawable/logo_meera.xml)
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(MeeraGold),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
    model = R.drawable.logo_meera,
    contentDescription = "Logo MEERA TV",
    contentScale = ContentScale.Crop,
    modifier = Modifier
        .size(50.dp)
        .clip(CircleShape)
)

        }
        Spacer(Modifier.width(10.dp))
        Text(
    text = "MEERA TV",
    style = MaterialTheme.typography.headlineSmall,
    fontWeight = FontWeight.Bold,
    modifier = Modifier.weight(1f)
)
IconButton(
    onClick = { navController.navigate(Screen.About.route) }
) {
    Icon(
        imageVector = Icons.Default.Menu,
        contentDescription = "Menu"
    )
}
    }
}

@Composable
private fun LiveBanner(isLive: Boolean, title: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(enabled = isLive, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (isLive) MeeraLiveRed else MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = if (isLive) Color.White else MeeraGold)
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    if (isLive) "🔴 EN DIRECT" else "Pas de direct actuellement",
                    color = if (isLive) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                if (isLive && title != null) {
                    Text(title, color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun ProgramRow(label: String, title: String) {
    Row {
        Text("$label · ", fontWeight = FontWeight.Bold, color = MeeraGold)
        Text(title)
    }
}

@Composable
private fun ReplayCard(replay: Replay, onClick: () -> Unit) {
    Column(modifier = Modifier.width(150.dp).clickable(onClick = onClick)) {
        AsyncImage(
            model = replay.thumbnail(),
            contentDescription = replay.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(90.dp).clip(RoundedCornerShape(10.dp))
        )
        Spacer(Modifier.height(6.dp))
        Text(replay.title, maxLines = 2, style = MaterialTheme.typography.bodySmall)
    }
}
