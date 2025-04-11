package com.example.healthmap.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthmap.ui.component.Input

@Composable
fun FromScreen() {
    var username by remember { mutableStateOf("") }
    var activityName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Username
        Input(
            value = username,
            label = "Your Name",
            placeholder = "Enter your name here",
            leadingIcon = Icons.Outlined.AccountCircle,
            modifier = Modifier.fillMaxWidth(1f)
        ) { username = it }
        // Activity
        Input(
            value = activityName,
            label = "Your Activity",
            placeholder = "Enter your activity here",
            leadingIcon = Icons.Outlined.ThumbUp,
            modifier = Modifier.fillMaxWidth(1f)
        ) { activityName = it }
    }
}

@Composable
@Preview
fun PreviewFromScreen() {
    FromScreen()
}