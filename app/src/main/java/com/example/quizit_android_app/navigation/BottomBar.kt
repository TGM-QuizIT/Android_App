package com.example.quizit_android_app.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.RippleDefaults.RippleAlpha
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalMaterial3Api::class)
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

    val MyRippleConfig = RippleConfiguration(color = Color.Unspecified, rippleAlpha = RippleAlpha(0f, 0f, 0f, 0f))

    CompositionLocalProvider(LocalRippleConfiguration provides MyRippleConfig) {
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
                    } == true,
                    onClick = {
                        navController.navigate(screen.route)
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination?.hierarchy?.any {
                                    it.hasRoute(route = screen.route::class)
                                } == true) screen.filledIcon else screen.icon,
                            contentDescription = screen.title,
                        )
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    ),
                )
            }
        }
    }
}

