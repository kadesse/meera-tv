package com.meera.tv.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.meera.tv.Screen
import com.meera.tv.bottomNavItems

@Composable
fun MeeraBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(iconFor(screen), contentDescription = screen.label) },
                label = { androidx.compose.material3.Text(screen.label) }
            )
        }
    }
}

private fun iconFor(screen: Screen) = when (screen) {
    Screen.Home -> Icons.Filled.Home
    Screen.Live -> Icons.Filled.FiberManualRecord
    Screen.Replays -> Icons.Filled.PlayCircle
    Screen.Programs -> Icons.Filled.Schedule
    Screen.Sermons -> Icons.Filled.MenuBook
    else -> Icons.Filled.Home
}
