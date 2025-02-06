package com.example.quizit_android_app.navigation

import android.os.Build
import android.util.Log
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
import com.example.quizit_android_app.model.Focus
import com.example.quizit_android_app.model.Subject
import com.example.quizit_android_app.ui.MainViewModel
import com.example.quizit_android_app.ui.home.HomeScreen
import com.example.quizit_android_app.ui.login.LoginScreen
import com.example.quizit_android_app.ui.play_quiz.focus.FocusScreen
import com.example.quizit_android_app.ui.play_quiz.quiz.QuizDetailScreen
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
                    navController.navigate("subject")
                },
                navigateToFocus = { subject->
                    navController.currentBackStackEntry?.savedStateHandle?.set("subject", subject)
                    navController.navigate("focus/${subject.subjectId}")
                },
                navigateToStatistics = {
                    navController.navigate("social/true")
                }
            )
        }
        composable("subject") {
            SubjectScreen(
                navigateToFocus = { subject ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("subject", subject)
                    navController.navigate("focus/${subject.subjectId}")
                },
                navigateBack = {
                    navController.navigate("home")
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

            val subject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("subject")
            Log.d("AppNavGraph", subject.toString())
            FocusScreen(
                navigateToQuiz = { subject, focus ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("focus", focus)
                    navController.currentBackStackEntry?.savedStateHandle?.set("subject", subject)


                    if(focus==null) {
                        navController.navigate("quiz/${subject?.subjectId}/true")
                    }
                    else {
                        navController.navigate("quiz/${focus.focusId}/false")
                    }
                },
                navigateBack = { subject2  ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("subject", subject2)
                    navController.navigate("subject")
                },
                navigateToQuizDetail = { subject, focus ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("focus", focus)
                    navController.currentBackStackEntry?.savedStateHandle?.set("subject", subject)

                    if(focus==null) {
                        navController.navigate("quiz_detail/${subject?.subjectId}/true")
                    }
                    else {
                        navController.navigate("quiz_detail/${focus.focusId}/false")
                    }

                },
                subject = subject

            )
        }

        composable("quiz/{id}/{isQuizOfSubject}") { backStackEntry ->
            val subject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("subject")
            val focus= navController.previousBackStackEntry?.savedStateHandle?.get<Focus>("focus")
            QuizScreen(
                navigateBack = { subject2, focus2 ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("subject", subject2)
                    navController.currentBackStackEntry?.savedStateHandle?.set("focus", focus2)
                    navController.navigate("focus/${subject2?.subjectId}")
                },
                subject = subject,
                focus = focus,
            )
        }

        composable("quiz_detail/{id}/{isDetailOfSubject}") { backStackEntry ->
            val subject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("subject")
            val focus= navController.previousBackStackEntry?.savedStateHandle?.get<Focus>("focus")
            QuizDetailScreen(
                subject = subject!!,
                focus = focus
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