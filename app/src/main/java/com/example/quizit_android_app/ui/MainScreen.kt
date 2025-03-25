package com.example.quizit_android_app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quizit_android_app.navigation.AppNavGraph
import com.example.quizit_android_app.navigation.BottomBar
import com.example.quizit_android_app.navigation.LoginRoute
import com.example.quizit_android_app.navigation.QuizRoute
import com.example.quizit_android_app.ui.login.LoginScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            val routes = listOf(LoginRoute::class, QuizRoute::class)

            if(
                currentDestination != null &&
                !currentDestination.hierarchy.any { destination ->
                    routes.any { route -> destination.hasRoute(route) }
                }
            ) {
                BottomBar(navController = navController)

            }

        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            AppNavGraph(navController = navController)
        }
    }
}
