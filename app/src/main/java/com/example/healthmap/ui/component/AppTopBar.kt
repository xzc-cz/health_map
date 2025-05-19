package com.example.healthmap.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    isHomeScreen: Boolean = false,
    showNavigationIcon: Boolean = true,
    onNavigationClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = if (isHomeScreen) Icons.Default.Menu else Icons.Default.ArrowBack,
                        contentDescription = if (isHomeScreen) "Menu" else "Back",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black
        )
    )
}
