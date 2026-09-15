package com.meera.tv.ui.screens.programs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meera.tv.data.model.Program
import com.meera.tv.data.repository.MeeraRepository
import com.meera.tv.ui.theme.MeeraGold

@Composable
fun ProgramsScreen(repository: MeeraRepository = MeeraRepository()) {
    var programs by remember { mutableStateOf<List<Program>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        runCatching { repository.getPrograms() }.onSuccess { programs = it }
        loading = false
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Programmes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (programs.isEmpty()) {
            Text("Aucun programme prévu pour le moment.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(programs) { program ->
                    Card {
                        Column(Modifier.padding(12.dp)) {
                            Text(program.title, fontWeight = FontWeight.Bold)
                            Text(
                                "${program.startTime} → ${program.endTime}",
                                color = MeeraGold,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(program.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
