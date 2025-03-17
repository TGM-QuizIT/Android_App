package com.example.quizit_android_app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: Any,
    val title: String,
    val icon: ImageVector,
    val filledIcon: ImageVector
) {
    object Home: BottomBarScreen(
        route = com.example.quizit_android_app.navigation.HomeRoute,
        title = "Home",
        icon = Icons.Outlined.Home,
        filledIcon = Icons.Filled.Home
    )
    object Settings: BottomBarScreen(
        route = com.example.quizit_android_app.navigation.SettingsRoute,
        title = "Settings",
        icon = Icons.Outlined.Settings,
        filledIcon = Icons.Filled.Settings
    )
    object Friends: BottomBarScreen(
        route = SocialRoute(),
        title = "Social",
        icon = Icons.Outlined.Group,
        filledIcon = Icons.Filled.Group
    )
    object Subject: BottomBarScreen(
        route = com.example.quizit_android_app.navigation.SubjectRoute,
        title = "Quiz",
        icon = Icons.Outlined.Book,
        filledIcon = Icons.Filled.Book
    )
}