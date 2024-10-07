package com.example.quizit_android_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontVariation
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizit_android_app.ui.friends.FriendsScreen
import com.example.quizit_android_app.ui.home.HomeScreen
import com.example.quizit_android_app.ui.quiz.QuizScreen
import com.example.quizit_android_app.ui.settings.SettingsScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = BottomBarScreen.Home.route
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen()
        }

        composable(route = BottomBarScreen.Quiz.route) {
            QuizScreen()
        }

        composable(route = BottomBarScreen.Friends.route) {
            FriendsScreen()
        }

        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen()
        }

    }
}