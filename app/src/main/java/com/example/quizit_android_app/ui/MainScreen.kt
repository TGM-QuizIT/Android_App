package com.example.quizit_android_app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quizit_android_app.navigation.AppNavGraph
import com.example.quizit_android_app.navigation.BottomBar
import com.example.quizit_android_app.ui.login.LoginScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentDestination !in listOf("login", "quiz/{focusId}")) {
                BottomBar(navController = navController)
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            AppNavGraph(navController = navController)
        }
    }



}
