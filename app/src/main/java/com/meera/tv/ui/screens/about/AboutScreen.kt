package com.meera.tv.ui.screens.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "J-C TV",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Ministère Évangélique Ébénézer pour la Restauration des Âmes"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ebenezer confiance"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Retrouvez-nous sur YouTube, Facebook et TikTok."
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/@MEERATV2")
                )
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("YouTube J-C TV")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Culte de feu : vendredi à 19h\nCulte dominical : dimanche à 08h"
        )
    }
}
