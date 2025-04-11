package com.example.healthmap.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.healthmap.model.Plan
import com.example.healthmap.repository.PlanRepository
import com.mapbox.geojson.Point
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.viewannotation.annotationAnchor
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import java.time.LocalDate
import java.util.*

@Composable
fun MapScreen() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val plans = remember(selectedDate) { PlanRepository.getPlansForDate(selectedDate) }
    val list = PlanRepository.getPlansForDate(selectedDate)
    println("📍 Loaded ${list.size} plans for $selectedDate")
    //调试

    Column(modifier = Modifier.fillMaxSize()) {
        // 日期选择器按钮
        DatePickerHeader(selectedDate) { pickedDate ->
            selectedDate = pickedDate
        }

        // 地图区域
        Box(modifier = Modifier.weight(1f)) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = rememberMapViewportState {
                    setCameraOptions {
                        zoom(14.0)
                        center(Point.fromLngLat(145.130, -37.917))
                    }
                }
            ) {
                plans.forEach { plan ->
                    //调试
                    println("🗺 Showing plan: ${plan.name} at (${plan.lng}, ${plan.lat})")
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
            .padding(16.dp)
    ) {
        Text("选择日期：$selectedDate")
    }
}
