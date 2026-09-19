package com.meera.tv.ui.screens.contact

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ContactScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            "Contact",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Text("📞 Téléphone : +225 05 01 96 56 57")

        Spacer(Modifier.height(8.dp))

        Text("✉️ E-mail : kadesse3@gmail.com")

        Spacer(Modifier.height(8.dp))

        Text("📍 Ebimpé, Anyama, Côte d’Ivoire")
    }
}