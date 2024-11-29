package com.example.quizit_android_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizit_android_app.ui.friends.FriendsScreen
import com.example.quizit_android_app.ui.home.HomeScreen
import com.example.quizit_android_app.ui.play_quiz.focus.FocusScreen
import com.example.quizit_android_app.ui.play_quiz.subject.SubjectScreen
import com.example.quizit_android_app.ui.quiz.QuizScreen
import com.example.quizit_android_app.ui.settings.SettingsScreen

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navigateToSubject ={
                    navController.navigate("subject")
                },
                navigateToFocus = { id->
                    navController.navigate("focus/$id")
                },
            )
        }
        composable("quiz") {
            SubjectScreen(
                navigateToFocus = { id ->
                    navController.navigate("focus/$id")
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("friends") {
            FriendsScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
        composable("focus/{subjectId}") { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getString("focusId")?.toInt() ?: 0
            FocusScreen(
                subjectId = subjectId,
                navigateToQuiz = { id ->
                    navController.navigate("quiz/$id")
                },
                navigateBack = {
                    navController.popBackStack()
                }

            )
        }

        composable("quiz/{focusId}") { backStackEntry ->
            val focusId = backStackEntry.arguments?.getString("focusId")?.toInt() ?: 0
            QuizScreen(
                focusId = focusId,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}