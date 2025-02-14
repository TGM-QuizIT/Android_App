package com.example.quizit_android_app.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quizit_android_app.model.retrofit.CustomNavType
import com.example.quizit_android_app.model.retrofit.Focus
import com.example.quizit_android_app.model.retrofit.Subject
import com.example.quizit_android_app.model.retrofit.User
import com.example.quizit_android_app.ui.MainViewModel
import com.example.quizit_android_app.ui.home.HomeScreen
import com.example.quizit_android_app.ui.login.LoginScreen
import com.example.quizit_android_app.ui.play_quiz.focus.FocusScreen
import com.example.quizit_android_app.ui.play_quiz.subject.SubjectScreen
import com.example.quizit_android_app.ui.quiz.QuizScreen
import com.example.quizit_android_app.ui.settings.SettingsScreen
import com.example.quizit_android_app.ui.social.SocialScreen
import com.example.quizit_android_app.ui.social.UserDetailScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf


@Serializable
object HomeRoute

@Serializable
object LoginRoute

@Serializable
object SubjectRoute

@Serializable
object SettingsRoute

@Serializable
data class FocusRoute(val subject: Subject) {
    companion object {
        val typeMap = mapOf(typeOf<Subject>() to CustomNavType.SubjectType)

        fun from(savedStateHandle: SavedStateHandle) =
            savedStateHandle.toRoute<FocusRoute>(typeMap)
    }
}


@Serializable
data class QuizRoute(
    val focus: Focus?,
    val subject: Subject
) {
    companion object {
        val typeMap = mapOf(
            typeOf<Focus?>() to CustomNavType.FocusType,
            typeOf<Subject>() to CustomNavType.SubjectType
        )

        fun from(savedStateHandle: SavedStateHandle) =
            savedStateHandle.toRoute<QuizRoute>(typeMap)
    } 
}

@Serializable
data class SocialRoute(
    val showStatistics: Boolean = false
)

@Serializable
data class UserDetailRoute(
    val friendshipId: Int?,
    val user: User,
) {
    companion object {
        val typeMap = mapOf(
            typeOf<User>() to CustomNavType.UserType
        )

        fun from(savedStateHandle: SavedStateHandle) =
            savedStateHandle.toRoute<UserDetailRoute>(typeMap)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController(), viewModel: MainViewModel = hiltViewModel()) {

    val isLoggedIn = viewModel.isLoggedIn.value

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(HomeRoute) {
                popUpTo(HomeRoute) { inclusive = true }
            }
        } else {
            navController.navigate(LoginRoute) {
                popUpTo(LoginRoute) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = if (isLoggedIn) HomeRoute else LoginRoute) {
        composable<HomeRoute> {
            HomeScreen(
                navigateToSubject ={
                    navController.navigate(com.example.quizit_android_app.navigation.SubjectRoute)
                },
                navigateToFocus = { subject->
                    Log.d("AppNavGraph", FocusRoute(subject).toString())
                    navController.navigate(FocusRoute(subject))
                },
                navigateToChallenge = {
                    navController.navigate(SocialRoute())
                },
                navigateToStatistics = {
                    navController.navigate(SocialRoute(true))
                }
            )
        }
        composable<com.example.quizit_android_app.navigation.SubjectRoute> {
            SubjectScreen(
                navigateToFocus = { subject ->
                    navController.navigate(FocusRoute(subject))
                },
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                onLogout = {
                    viewModel.setLoggedIn(false)
                }
            )
        }
        composable<com.example.quizit_android_app.navigation.FocusRoute>(
            typeMap = FocusRoute.typeMap
        ) {

            FocusScreen(
                navigateToQuiz = { subject, focus ->
                    Log.d("AppNavGraph", QuizRoute(focus, subject).toString())
                    navController.navigate(QuizRoute(focus, subject))
                },
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToQuizDetail = { subject, focus ->
                    //TODO
                },


            )
        }

        composable<QuizRoute>(
            typeMap = QuizRoute.typeMap
        ) {
            QuizScreen(
                navigateBack = {
                    navController.popBackStack()
                },
            )
        }



        composable<SocialRoute> {
            SocialScreen(
                navigateToUserDetail = { id, user ->
                    Log.d("AppNavGraph", UserDetailRoute(id, user).toString())
                    navController.navigate(UserDetailRoute(friendshipId = id, user= user))

                }
            )
        }



        composable<UserDetailRoute>(
            typeMap = UserDetailRoute.typeMap
        ) {
            UserDetailScreen(
                onGoBack = {
                    navController.popBackStack()
                }
            )

        }

        composable<LoginRoute> {
            LoginScreen(onLoginSuccess = { viewModel.setLoggedIn(true) })
        }

    }
}

