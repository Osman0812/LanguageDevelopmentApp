package com.example.languagedevelopmentapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

open class BottomBarScreen(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    data object Vocabulary : BottomBarScreen(
        route = "VOCABULARY",
        icon = Icons.Default.List,
        title = "My Words"
    )
    data object Home : BottomBarScreen(
        route = "HOME",
        icon = Icons.Default.Home,
        title = "Home"
    )
    data object Practice : BottomBarScreen(
        route = "PRACTICE",
        icon = Icons.Default.Star,
        title = "Practice"
    )
    data object Profile : BottomBarScreen(
        route = "PROFILE",
        icon = Icons.Default.Person,
        title = "Profile"
    )
}