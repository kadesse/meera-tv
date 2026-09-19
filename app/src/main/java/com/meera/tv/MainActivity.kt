package com.meera.tv

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.meera.tv.ui.components.MeeraBottomBar
import com.meera.tv.ui.screens.about.AboutScreen
import com.meera.tv.ui.screens.contact.ContactScreen
import com.meera.tv.ui.screens.home.HomeScreen
import com.meera.tv.ui.screens.live.LiveScreen
import com.meera.tv.ui.screens.notifications.NotificationsScreen
import com.meera.tv.ui.screens.prayer.PrayerScreen
import com.meera.tv.ui.screens.programs.ProgramsScreen
import com.meera.tv.ui.screens.replays.ReplayDetailScreen
import com.meera.tv.ui.screens.replays.ReplaysScreen
import com.meera.tv.ui.screens.sermons.SermonsScreen
import com.meera.tv.ui.screens.word.WordOfGodScreen
import com.meera.tv.ui.theme.MeeraTvTheme
import com.meera.tv.update.UpdateChecker
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MeeraTvTheme {
                MeeraTvNavigation()
            }
        }

        lifecycleScope.launch {
            val update = UpdateChecker.check()

            if (update != null) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Nouvelle version J-C TV ${update.versionName} disponible",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(update.apkUrl)
                        )
                    )
                }
            }
        }
    }
}

sealed class Screen(
    val route: String,
    val label: String
) {
    data object Home : Screen("home", "Accueil")
    data object Live : Screen("live", "Direct")
    data object Replays : Screen("replays", "Replays")
    data object ReplayDetail : Screen("replay_detail/{videoId}", "Lecture")
    data object Programs : Screen("programs", "Programmes")
    data object Sermons : Screen("sermons", "Prédications")
    data object Prayer : Screen("prayer", "Prière")
    data object WordOfGod : Screen("word_of_god", "Parole de Dieu")
    data object Notifications : Screen("notifications", "Notifications")
    data object About : Screen("about", "À propos")
    data object Contact : Screen("contact", "Contact")
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Live,
    Screen.Replays,
    Screen.Programs,
    Screen.Sermons
)

@Composable
fun MeeraTvNavigation() {
    val navController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            MeeraBottomBar(navController)
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screen.Home.route) {
                HomeScreen(navController)
            }

            composable(Screen.Live.route) {
                LiveScreen()
            }

            composable(Screen.Replays.route) {
                ReplaysScreen(navController)
            }

            composable(Screen.ReplayDetail.route) { backStackEntry ->
                val videoId =
                    backStackEntry.arguments?.getString("videoId") ?: ""

                ReplayDetailScreen(videoId)
            }

            composable(Screen.Programs.route) {
                ProgramsScreen()
            }

            composable(Screen.Sermons.route) {
                SermonsScreen(navController)
            }

            composable(Screen.Prayer.route) {
                PrayerScreen()
            }

            composable(Screen.WordOfGod.route) {
                WordOfGodScreen()
            }

            composable(Screen.Notifications.route) {
                NotificationsScreen()
            }

            composable(Screen.About.route) {
                AboutScreen()
            }

            composable(Screen.Contact.route) {
                ContactScreen()
            }
        }
    }
}