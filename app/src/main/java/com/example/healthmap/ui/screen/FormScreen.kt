package com.example.healthmap.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthmap.R
import com.example.healthmap.model.Plan
import com.example.healthmap.ui.component.AppTopBar
import com.example.healthmap.ui.component.Input
import com.example.healthmap.ui.component.InputType
import com.example.healthmap.ui.component.PlanMarkerText
import com.example.healthmap.viewmodel.PlanViewModel
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
fun FormScreen(
    navController: NavController,
    userName: String,
    planViewModel: PlanViewModel = viewModel()
) {
    val planCount by planViewModel.getCount().collectAsState(initial = 0)
    val context = LocalContext.current

    var showMap by remember { mutableStateOf(true) }
    var activityName by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(14.0)
            center(Point.fromLngLat(longitude, latitude))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = "Form Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Make a New Plan",
                    isHomeScreen = false,
                    onNavigationClick = {
                        showMap = false
                        navController.popBackStack()
                    }
                )
            },
            containerColor = Color.Transparent
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
                    Input(
                        value = activityName,
                        label = "Your Activity",
                        placeholder = "Enter your activity here",
                        leadingIcon = Icons.Outlined.ThumbUp,
                        modifier = Modifier.fillMaxWidth()
                    ) { activityName = it.toString() }

                    Spacer(modifier = Modifier.height(12.dp))

                    Input(
                        value = selectedTime,
                        label = "Select Time",
                        type = InputType.TIME,
                        modifier = Modifier.fillMaxWidth()
                    ) { selectedTime = it as LocalTime }

                    Spacer(modifier = Modifier.height(12.dp))

                    Input(
                        value = selectedDate,
                        label = "Select Date",
                        type = InputType.DATE,
                        modifier = Modifier.fillMaxWidth()
                    ) { selectedDate = it as LocalDate }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            Input(
                                value = longitude,
                                label = "Longitude",
                                type = InputType.NUMBER,
                                leadingIcon = Icons.Outlined.LocationOn,
                                modifier = Modifier
                            ) {
                                longitude = it as Double
                                if (latitude in -90.0..90.0 && longitude in -180.0..180.0) {
                                    mapViewportState.flyTo(
                                        CameraOptions.Builder()
                                            .center(Point.fromLngLat(longitude, latitude))
                                            .zoom(14.0)
                                            .build()
                                    )
                                } else {
                                    Toast.makeText(context, "Invalid coordinates", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            Input(
                                value = latitude,
                                label = "Latitude",
                                type = InputType.NUMBER,
                                leadingIcon = Icons.Outlined.LocationOn,
                                modifier = Modifier
                            ) {
                                latitude = it as Double
                                if (latitude in -90.0..90.0 && longitude in -180.0..180.0) {
                                    mapViewportState.flyTo(
                                        CameraOptions.Builder()
                                            .center(Point.fromLngLat(longitude, latitude))
                                            .zoom(14.0)
                                            .build()
                                    )
                                } else {
                                    Toast.makeText(context, "Invalid coordinates", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    if (showMap) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(10.dp)
                                .border(0.dp, Color.Transparent, shape = RectangleShape),
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
                                        if (userName.isNotEmpty()) userName else "Your Name",
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
                        .background(Color.Black, shape = RoundedCornerShape(12.dp))
                        .clickable {
                            if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) {
                                Toast.makeText(context, "Coordinates out of range", Toast.LENGTH_SHORT).show()
                                return@clickable
                            }

                            submitPlan(
                                planCount,
                                userName,
                                activityName,
                                timeToString(selectedTime),
                                selectedDate,
                                longitude,
                                latitude,
                                planViewModel
                            )
                            navController.navigate("home")
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
}

private fun timeToString(time: LocalTime): String {
    val hour = time.hour
    val minute = time.minute
    return "${if (hour < 10) "0$hour" else hour}:${if (minute < 10) "0$minute" else minute}"
}

private fun submitPlan(
    id: Int,
    username: String,
    activity: String,
    time: String,
    date: LocalDate,
    longitude: Double,
    latitude: Double,
    planViewModel: PlanViewModel
) {
    val plan = Plan(
        id = id,
        name = username,
        activity = activity,
        time = time,
        date = date,
        lng = longitude,
        lat = latitude
    )
    planViewModel.insertPlan(plan)
}