package com.meera.tv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Identité visuelle MEERA TV : bleu nuit profond + or/ambre, sobre et lisible
val MeeraNavy = Color(0xFF0B1F3A)
val MeeraNavyLight = Color(0xFF16305A)
val MeeraGold = Color(0xFFD4AF37)
val MeeraGoldLight = Color(0xFFE8CC6E)
val MeeraLiveRed = Color(0xFFE0303A)
val MeeraBackground = Color(0xFF0A1626)
val MeeraSurface = Color(0xFF122240)
val MeeraOnSurface = Color(0xFFF4F1E8)

private val MeeraDarkColors = darkColorScheme(
    primary = MeeraGold,
    onPrimary = MeeraNavy,
    secondary = MeeraGoldLight,
    background = MeeraBackground,
    onBackground = MeeraOnSurface,
    surface = MeeraSurface,
    onSurface = MeeraOnSurface,
    error = MeeraLiveRed
)

private val MeeraLightColors = lightColorScheme(
    primary = MeeraNavy,
    onPrimary = Color.White,
    secondary = MeeraGold,
    background = Color(0xFFFAF9F5),
    onBackground = MeeraNavy,
    surface = Color.White,
    onSurface = MeeraNavy,
    error = MeeraLiveRed
)

val MeeraTitleStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp, letterSpacing = 0.5.sp)
val MeeraSectionStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

@Composable
fun MeeraTvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) MeeraDarkColors else MeeraLightColors
    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
