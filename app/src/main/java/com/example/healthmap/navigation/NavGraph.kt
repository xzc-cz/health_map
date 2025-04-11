package com.example.healthmap.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.healthmap.ui.screen.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("form") {
            FormScreen()
        }
        composable("map") {
            MapScreen()
        }
        composable("login") {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {
    TODO("Not yet implemented")
}

@Composable
fun FormScreen() {
    TODO("Not yet implemented")
}
