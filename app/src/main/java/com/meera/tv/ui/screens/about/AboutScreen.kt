package com.meera.tv.ui.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meera.tv.BuildConfig

@Composable
fun AboutScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("À propos de MEERA TV", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text(
            "MEERA TV est la plateforme de télévision chrétienne numérique du " +
                "Ministère Évangélique Ébénézer pour la Restauration des Âmes (MEERA). " +
                "Elle diffuse des cultes, prédications, enseignements et émissions en direct et en replay."
        )
        Spacer(Modifier.height(16.dp))
        Text("Version de l'application : ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.bodySmall)
    }
}
