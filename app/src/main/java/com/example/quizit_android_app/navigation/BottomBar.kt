package com.example.quizit_android_app.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.reflect.KClass

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Subject,
        BottomBarScreen.Friends,
        BottomBarScreen.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier
            .border(BorderStroke(1.dp, Color.LightGray)),
        containerColor = Color.White
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(route = screen.route::class)
                }==true,
                onClick = { navController.navigate(screen.route) },
                icon =  {
                    Icon(
                        imageVector = if (currentDestination?.hierarchy?.any {
                            it.hasRoute(route = screen.route::class)
                        }==true) screen.filledIcon else screen.icon,
                        contentDescription = screen.title,
                    )
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}