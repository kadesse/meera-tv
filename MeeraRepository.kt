package com.meera.tv.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.meera.tv.data.model.Announcement
import com.meera.tv.data.model.LiveStatus
import com.meera.tv.data.model.PrayerRequest
import com.meera.tv.data.model.Program
import com.meera.tv.data.model.Replay
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Accès aux données via Firestore directement depuis l'app — pas de serveur
 * intermédiaire à payer ni à maintenir. La structure des collections est :
 *
 *   config/liveStatus        (document unique)
 *   replays/{id}
 *   programs/{id}
 *   announcements/{id}
 *   prayerRequests/{id}      (écriture publique, lecture admin seulement)
 *
 * Les règles de sécurité Firestore (voir /meera-admin-web/firestore.rules)
 * autorisent la lecture publique de tout sauf prayerRequests, et n'autorisent
 * l'écriture qu'aux comptes admin connectés (sauf la création d'une nouvelle
 * demande de prière, ouverte à tous).
 */
class MeeraRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getLiveStatus(): LiveStatus {
        val doc = db.collection("config").document("liveStatus").get().await()
        return doc.toObject(LiveStatus::class.java) ?: LiveStatus()
    }

    suspend fun getReplays(category: String? = null): List<Replay> {
        var query: Query = db.collection("replays").orderBy("date", Query.Direction.DESCENDING)
        if (category != null) query = query.whereEqualTo("category", category)
        val snapshot = query.limit(100).get().await()
        return snapshot.documents.mapNotNull { d -> d.toObject(Replay::class.java)?.copy(id = d.id) }
    }

    suspend fun getReplayById(id: String): Replay? {
        val doc = db.collection("replays").document(id).get().await()
        return doc.toObject(Replay::class.java)?.copy(id = doc.id)
    }

    suspend fun getCurrentProgram(): Program? {
        val nowIso = isoNow()
        val snapshot = db.collection("programs")
            .whereLessThanOrEqualTo("startTime", nowIso)
            .orderBy("startTime", Query.Direction.DESCENDING)
            .limit(5)
            .get().await()
        return snapshot.documents
            .mapNotNull { d -> d.toObject(Program::class.java)?.copy(id = d.id) }
            .firstOrNull { it.endTime >= nowIso }
    }

    suspend fun getNextProgram(): Program? {
        val nowIso = isoNow()
        val snapshot = db.collection("programs")
            .whereGreaterThan("startTime", nowIso)
            .orderBy("startTime", Query.Direction.ASCENDING)
            .limit(1)
            .get().await()
        return snapshot.documents.firstOrNull()?.let { d -> d.toObject(Program::class.java)?.copy(id = d.id) }
    }

    suspend fun getPrograms(): List<Program> {
        val snapshot = db.collection("programs").orderBy("startTime").limit(50).get().await()
        return snapshot.documents.mapNotNull { d -> d.toObject(Program::class.java)?.copy(id = d.id) }
    }

    suspend fun getAnnouncements(): List<Announcement> {
        val snapshot = db.collection("announcements")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(20).get().await()
        return snapshot.documents.mapNotNull { d -> d.toObject(Announcement::class.java)?.copy(id = d.id) }
    }

    suspend fun submitPrayerRequest(request: PrayerRequest) {
        db.collection("prayerRequests").add(request.copy(createdAt = isoNow())).await()
    }

    private fun isoNow(): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(Date())
    }
}
