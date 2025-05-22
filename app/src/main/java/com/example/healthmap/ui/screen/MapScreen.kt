package com.example.healthmap.ui.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.healthmap.ui.component.PlanMarkerText
import com.example.healthmap.viewmodel.PlanViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.annotationAnchor
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import java.time.LocalDate
import java.util.Calendar
import com.example.healthmap.ui.component.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    lat: Double,
    lng: Double,
    planViewModel: PlanViewModel = viewModel()
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val plans by planViewModel.getPlansForDate(selectedDate).collectAsState(initial = emptyList())
    val context = LocalContext.current
    println("📍 Loaded ${plans.size} plans for $selectedDate")

    Scaffold(
        topBar = {
            AppTopBar(
                title = "View On Map",
                isHomeScreen = false,
                onNavigationClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        },
                        selectedDate.year,
                        selectedDate.monthValue - 1,
                        selectedDate.dayOfMonth
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Choose the date：$selectedDate")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            Box(modifier = Modifier.weight(1f)) {
                MapboxMap(
                    modifier = Modifier.fillMaxSize(),
                    mapViewportState = rememberMapViewportState {
                        setCameraOptions {
                            zoom(14.0)
                            center(Point.fromLngLat(lng, lat))
                        }
                    }
                ) {
                    plans.forEach { plan ->
                        ViewAnnotation(
                            options = viewAnnotationOptions {
                                geometry(Point.fromLngLat(plan.lng, plan.lat))
                                annotationAnchor {
                                    anchor(ViewAnnotationAnchor.BOTTOM)
                                }
                            }
                        ) {
                            PlanMarkerText(plan.name, plan.activity, plan.time)
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun DatePickerHeader(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        set(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)
    }

    Button(
        onClick = {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        shape = RectangleShape
    ) {
        Text("Choose the Date：$selectedDate")
    }
}