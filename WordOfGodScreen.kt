package com.meera.tv.ui.screens.word

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Écran "Parole de Dieu" : verset/message du jour.
 * Le contenu proviendra de l'API (endpoint à ajouter côté backend,
 * ex. GET /api/word-of-the-day) et sera gérable depuis l'admin, au même titre
 * que les annonces.
 */
@Composable
fun WordOfGodScreen() {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Parole de Dieu", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))
        Card {
            Column(Modifier.padding(20.dp)) {
                Text(
                    "« Car je connais les projets que j'ai formés sur vous... des projets de paix et non de malheur, afin de vous donner un avenir et de l'espérance. »",
                    fontStyle = FontStyle.Italic
                )
                Spacer(Modifier.height(8.dp))
                Text("Jérémie 29:11", fontWeight = FontWeight.Bold)
            }
        }
    }
}
