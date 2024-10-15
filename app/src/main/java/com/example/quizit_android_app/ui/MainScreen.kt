package com.example.quizit_android_app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.quizit_android_app.navigation.BottomBar
import com.example.quizit_android_app.navigation.BottomNavGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController=navController)}
    ) {
        Box(modifier = Modifier.padding(it)) {
            BottomNavGraph(navController = navController)
        }
    }
}
