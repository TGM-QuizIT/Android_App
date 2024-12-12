package com.example.quizit_android_app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizit_android_app.ui.home.HomeScreen
import com.example.quizit_android_app.ui.play_quiz.focus.FocusScreen
import com.example.quizit_android_app.ui.play_quiz.subject.SubjectScreen
import com.example.quizit_android_app.ui.quiz.QuizScreen
import com.example.quizit_android_app.ui.settings.SettingsScreen
import com.example.quizit_android_app.ui.social.AddFriendScreen
import com.example.quizit_android_app.ui.social.SocialScreen
import com.example.quizit_android_app.ui.social.UserDetailScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navigateToSubject ={
                    navController.navigate("quiz")
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

        composable("settings") {
            SettingsScreen()
        }
        composable("focus/{subjectId}") { backStackEntry ->
            FocusScreen(
                navigateToQuiz = { id ->
                    navController.navigate("quiz/$id")
                },
                navigateBack = {
                    navController.popBackStack()
                }

            )
        }

        composable("quiz/{focusId}") { backStackEntry ->
            QuizScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("social") {
            SocialScreen(
                onAdd = {
                    navController.navigate("social/add")
                },
                navigateToUserDetail = { userId ->
                    navController.navigate("social/$userId")
                }
            )
        }
        composable("social/add") {
            AddFriendScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )

        }

        composable("social/{userId}") {
            UserDetailScreen(
                onGoBack = {
                    navController.popBackStack()
                }
            )

        }
    }
}