package com.example.healthmap.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.healthmap.ui.screen.FormScreen
import com.example.healthmap.ui.screen.HomeScreen
import com.example.healthmap.ui.screen.LoginScreen
import com.example.healthmap.ui.screen.MapScreen
import com.example.healthmap.ui.screen.ProfileScreen
import com.example.healthmap.ui.screen.RegisterScreen
import com.example.healthmap.ui.screen.ResetScreen


@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("home") {
            HomeScreen(userName = "Student 1",navController)
        }
        composable(
            route = "form/{userName}",
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            FormScreen(navController = navController, userName = userName)
        }
        composable("map") {
            MapScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("reset") {
            ResetScreen(navController)
        }
    }
}

