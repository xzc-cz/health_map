package com.example.healthmap.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.healthmap.R
import com.example.healthmap.ui.component.AppTopBar

@Composable
fun AboutAppScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.home_background), // ⬅️ 替换成你自己的图片
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                title = "About App",
                isHomeScreen = false,
                onNavigationClick = { navController.popBackStack() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "About Health-Map",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "HealthMap is a context-aware and accessible Android application designed to support " +
                            "users with dual needs: maintaining physical health through scheduled outdoor or " +
                            "gym-based activities, and enhancing social engagement via shared or location-based " +
                            "planning.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "The app allows users to input, visualize, and manage daily plans on an " +
                            "interactive map, with future integration of calendar-based context to personalize " +
                            "suggestions. It encourages users to stay active by not only tracking personal goals " +
                            "but also seeing what others are planning around them, which adds a sense of " +
                            "community motivation.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}