package com.meera.tv.update

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class AppUpdate(
    val versionCode: Int,
    val versionName: String,
    val apkUrl: String,
    val releaseNotes: String
)

object UpdateChecker {

    private const val UPDATE_URL =
        "https://raw.githubusercontent.com/kadesse/meera-tv/main/update.json"

    suspend fun check(): AppUpdate? = withContext(Dispatchers.IO) {
        runCatching {
            val connection =
                (URL(UPDATE_URL).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 8_000
                    readTimeout = 8_000
                    setRequestProperty("Accept", "application/json")
                }

            try {
                if (connection.responseCode !in 200..299) {
                    return@runCatching null
                }

                val json = connection.inputStream.bufferedReader().use {
                    it.readText()
                }

                val data = JSONObject(json)

                val remoteCode = data.optInt("versionCode", 0)
                val remoteName = data.optString("versionName", "")
                val apkUrl = data.optString("apkUrl", "")
                val notes = data.optString("releaseNotes", "")

                if (
                    remoteCode > 213 &&
                    remoteName.isNotBlank() &&
                    apkUrl.startsWith("https://")
                ) {
                    AppUpdate(
                        versionCode = remoteCode,
                        versionName = remoteName,
                        apkUrl = apkUrl,
                        releaseNotes = notes
                    )
                } else {
                    null
                }
            } finally {
                connection.disconnect()
            }
        }.getOrNull()
    }
}