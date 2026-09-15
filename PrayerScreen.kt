package com.meera.tv.ui.screens.prayer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var sent by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Demande de prière", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Partagez votre besoin de prière, notre équipe prie pour vous.")
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Votre nom") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("Téléphone ou e-mail (optionnel)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Votre demande de prière") },
            modifier = Modifier.fillMaxWidth().height(140.dp)
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                sending = true
                scope.launch {
                    runCatching {
                        repository.submitPrayerRequest(
                            PrayerRequest(name = name, phoneOrEmail = contact.ifBlank { null }, message = message)
                        )
                    }
                    sending = false
                    sent = true
                }
            },
            enabled = !sending && message.isNotBlank() && name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (sending) CircularProgressIndicator(modifier = Modifier.height(20.dp)) else Text("Envoyer")
        }

        if (sent) {
            Spacer(Modifier.height(12.dp))
            Text("🙏 Votre demande a bien été reçue. Merci de votre confiance.")
        }
    }
}
