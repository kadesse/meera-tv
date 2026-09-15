package com.meera.tv.player

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Lecteur vidéo MEERA TV : affiche une vidéo ou un direct YouTube via
 * l'iframe officiel YouTube dans une WebView.
 *
 * Pourquoi YouTube plutôt qu'un serveur vidéo à nous : hébergement et
 * bande passante gratuits et illimités, aucune infrastructure à payer ni à
 * maintenir. Il suffit de fournir l'identifiant de la vidéo (visible dans
 * l'URL YouTube, ex. pour https://youtube.com/watch?v=ABC123 l'ID est ABC123).
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubePlayer(
    videoId: String,
    autoPlay: Boolean = true,
    modifier: Modifier = Modifier
) {
    val embedHtml = remember(videoId) {
        """
        <html><body style="margin:0;padding:0;background:#000;">
        <iframe width="100%" height="100%"
            src="https://www.youtube.com/embed/$videoId?autoplay=${if (autoPlay) 1 else 0}&playsinline=1&rel=0"
            frameborder="0"
            allow="autoplay; encrypted-media; picture-in-picture"
            allowfullscreen>
        </iframe>
        </body></html>
        """.trimIndent()
    }

    AndroidView(
        modifier = modifier.fillMaxWidth().aspectRatio(16f / 9f),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                loadDataWithBaseURL("https://www.youtube.com", embedHtml, "text/html", "utf-8", null)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL("https://www.youtube.com", embedHtml, "text/html", "utf-8", null)
        }
    )
}
