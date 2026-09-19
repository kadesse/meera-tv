package com.meera.tv.ui.screens.prayer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meera.tv.data.model.PrayerRequest
import com.meera.tv.data.repository.MeeraRepository
import kotlinx.coroutines.launch

@Composable
fun PrayerScreen(repository: MeeraRepository = MeeraRepository()) {
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    var success by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Demande de prière",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Text("Partagez votre besoin de prière, notre équipe prie pour vous.")

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                error = null
                success = false
            },
            label = { Text("Votre nom") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = contact,
            onValueChange = {
                contact = it
                error = null
                success = false
            },
            label = { Text("Téléphone ou e-mail (optionnel)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = message,
            onValueChange = {
                message = it
                error = null
                success = false
            },
            label = { Text("Votre demande de prière") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                sending = true
                error = null
                success = false

                scope.launch {
                    try {
                        repository.submitPrayerRequest(
                            PrayerRequest(
                                name = name.trim(),
                                phoneOrEmail = contact.trim().ifBlank { null },
                                message = message.trim()
                            )
                        )

                        name = ""
                        contact = ""
                        message = ""
                        success = true

                    } catch (e: Exception) {
                        error = "Impossible d'envoyer votre demande. Vérifiez votre connexion."
                    } finally {
                        sending = false
                    }
                }
            },
            enabled = !sending &&
                    name.isNotBlank() &&
                    message.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (sending) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Envoyer ma demande")
            }
        }

        if (success) {
            Spacer(Modifier.height(12.dp))

            Text(
                "🙏 Votre demande a bien été reçue. Merci de votre confiance.",
                color = MaterialTheme.colorScheme.primary
            )
        }

        error?.let {
            Spacer(Modifier.height(12.dp))

            Text(
                it,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}