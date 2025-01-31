package com.example.quizit_android_app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Outlined.Home
    )
    object Settings: BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Outlined.Settings
    )
    object Friends: BottomBarScreen(
        route = "social",
        title = "Friends",
        icon = Icons.Outlined.Group
    )
    object Subject: BottomBarScreen(
        route = "subject",
        title = "Quiz",
        icon = Icons.Outlined.Book
    )
}