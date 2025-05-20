package com.example.healthmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.healthmap.navigation.AppNavGraph
import com.example.healthmap.ui.theme.HealthMapTheme
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            HealthMapTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}