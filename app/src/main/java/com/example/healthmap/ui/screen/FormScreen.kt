package com.example.healthmap.ui.screen

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import com.example.healthmap.model.Plan
import com.example.healthmap.ui.component.Input
import com.example.healthmap.ui.component.InputType
import com.example.healthmap.ui.component.PlanMarkerText
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.annotationAnchor
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(navController: NavController) {
    var showMap by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val view = LocalView.current
    val window = (context as ComponentActivity).window
    var username by remember { mutableStateOf("") }
    var activityName by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(14.0)
            center(Point.fromLngLat(longitude, latitude))
        }
    }

    SideEffect {
        window.statusBarColor = Color.Black.toArgb()
        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = false
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Make a New Plan",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    ) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            showMap = false
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Username
                Input(
                    value = username,
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
                // Location (longitude and latitude)
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        // Longitude
                        Input(
                            value = longitude,
                            label = "Longitude",
                            type = InputType.NUMBER,
                            leadingIcon = Icons.Outlined.LocationOn,
                            modifier = Modifier
                        ) {
                            longitude = it as Double
                            mapViewportState.flyTo(
                                CameraOptions.Builder()
                                    .center(Point.fromLngLat(longitude, latitude))
                                    .zoom(14.0)
                                    .build()
                            )
                        }
                    }
                    // Latitude
                    Box(
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Input(
                            value = latitude,
                            label = "Latitude",
                            type = InputType.NUMBER,
                            leadingIcon = Icons.Outlined.LocationOn,
                            modifier = Modifier
                        ) {
                            latitude = it as Double
                            mapViewportState.flyTo(
                                CameraOptions.Builder()
                                    .center(Point.fromLngLat(longitude, latitude))
                                    .zoom(14.0)
                                    .build()
                            )
                        }
                    }
                }
                if (showMap) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(240.dp)
                            .padding(10.dp)
                            .border(
                                width = 0.dp,
                                color = Color(0, 0, 0, 0),
                                shape = RectangleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        MapboxMap(
                            modifier = Modifier.fillMaxWidth().height(240.dp),
                            mapViewportState = mapViewportState
                        ) {
                            ViewAnnotation(
                                options = viewAnnotationOptions {
                                    geometry(Point.fromLngLat(longitude, latitude))
                                    annotationAnchor {
                                        anchor(ViewAnnotationAnchor.BOTTOM)
                                    }
                                }
                            ) {
                                PlanMarkerText(
                                    if (username.isNotEmpty()) username else "Your Name",
                                    if (activityName.isNotEmpty()) activityName else "Activity",
                                    timeToString(selectedTime)
                                )
                            }
                        }

                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(10.dp)
                    .background(Color.Black)
                    .clickable {
                        submitPlan(
                            username,
                            activityName,
                            timeToString(selectedTime),
                            selectedDate,
                            longitude,
                            latitude
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Submit",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

private fun timeToString(time: LocalTime): String {
    var hour = time.hour
    var minute = time.minute
    return "${
        if (hour < 10)
            "0$hour"
        else
            hour
    }:${
        if (minute < 10)
            "0$minute"
        else
            minute
    }"
}

private fun submitPlan(
    username: String,
    activity: String,
    time: String,
    date: LocalDate,
    longitude: Double,
    latitude: Double,
) {
    val plan = Plan(
        name = username,
        activity = activity,
        time = time,
        date = date,
        lng = longitude,
        lat = latitude
    )

    // TODO(Submit to database)
}