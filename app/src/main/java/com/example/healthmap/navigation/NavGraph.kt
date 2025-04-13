package com.example.healthmap.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.healthmap.ui.screen.FormScreen
import com.example.healthmap.ui.screen.HomeScreen
import com.example.healthmap.ui.screen.MapScreen

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
            FormScreen(navController)
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
