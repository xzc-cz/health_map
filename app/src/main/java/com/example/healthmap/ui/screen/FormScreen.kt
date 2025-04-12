package com.example.healthmap.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthmap.ui.component.Input
import com.example.healthmap.ui.component.InputType
import java.nio.file.WatchEvent
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun FormScreen() {
    var username by remember { mutableStateOf("") }
    var activityName by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Username
        Input(
            value = username as Object,
            label = "Your Name",
            placeholder = "Enter your name here",
            leadingIcon = Icons.Outlined.AccountCircle,
            modifier = Modifier.fillMaxWidth(1f)
        ) { username = it.toString() }
        // Activity
        Input(
            value = activityName,
            label = "Your Activity",
            placeholder = "Enter your activity here",
            leadingIcon = Icons.Outlined.ThumbUp,
            modifier = Modifier.fillMaxWidth(1f)
        ) { activityName = it.toString() }
        // Time
        Input(
            value = selectedTime,
            label = "Select Time",
            type = InputType.TIME,
            modifier = Modifier.fillMaxWidth(1f)
        ) { selectedTime = it as LocalTime }
        // Date
        Input(
            value = selectedDate,
            label = "Select Date",
            type = InputType.DATE,
            modifier = Modifier.fillMaxWidth(1f)
        ) { selectedDate = it as LocalDate }
        // Location (latitude and longitude)
        // Latitude
        Input(
            value = latitude,
            label = "Latitude",
            type = InputType.NUMBER,
            leadingIcon = Icons.Outlined.LocationOn,
            modifier = Modifier.fillMaxWidth(0.5f)
        ) { latitude = it as Double }
        // Longitude
        Input(
            value = longitude,
            label = "Longitude",
            type = InputType.NUMBER,
            leadingIcon = Icons.Outlined.LocationOn,
            modifier = Modifier.fillMaxWidth(0.5f)
        ) { longitude = it as Double }
    }
}

@Composable
@Preview
fun PreviewFromScreen() {
    FormScreen()
}