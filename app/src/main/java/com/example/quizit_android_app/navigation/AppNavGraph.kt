package com.example.quizit_android_app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizit_android_app.ui.MainViewModel
import com.example.quizit_android_app.ui.home.HomeScreen
import com.example.quizit_android_app.ui.login.LoginScreen
import com.example.quizit_android_app.ui.play_quiz.focus.FocusScreen
import com.example.quizit_android_app.ui.play_quiz.subject.SubjectScreen
import com.example.quizit_android_app.ui.quiz.QuizScreen
import com.example.quizit_android_app.ui.settings.SettingsScreen
import com.example.quizit_android_app.ui.social.SocialScreen
import com.example.quizit_android_app.ui.social.UserDetailScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController(), viewModel: MainViewModel = hiltViewModel()) {

    val isLoggedIn = viewModel.isLoggedIn.value

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = if (isLoggedIn) "home" else "login") {
        composable("home") {
            HomeScreen(
                navigateToSubject ={
                    navController.navigate("quiz")
                },
                navigateToFocus = { id->
                    navController.navigate("focus/$id")
                },
                navigateToStatistics = {
                    navController.navigate("social/true")
                }
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
            SettingsScreen(
                onLogout = {
                    viewModel.setLoggedIn(false)
                }
            )
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

        composable(
            "social/{showStatistics}",
            arguments = listOf(
                navArgument("showStatistics") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )

        ) {
            SocialScreen(
                navigateToUserDetail = { userId ->
                    navController.navigate("social/$userId")
                }
            )
        }

        composable("social",) {
            SocialScreen(
                navigateToUserDetail = { userId ->
                    navController.navigate("social/$userId")
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

        composable("login") {
            LoginScreen(onLoginSuccess = { viewModel.setLoggedIn(true) })
        }


    }
}