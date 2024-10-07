package com.example.quizit_android_app.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.quizit_android_app.navigation.BottomBar
import com.example.quizit_android_app.navigation.BottomNavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController=navController)}
    ) {
        BottomNavGraph(navController = navController)
    }
}
