package com.example.healthmap.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PlanMarkerText(name: String, activity: String, time: String) {
    Surface(
        modifier = Modifier.wrapContentSize(),
        shape = MaterialTheme.shapes.small,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "📍 $name", style = MaterialTheme.typography.titleSmall)
            Text(text = "$activity @ $time", style = MaterialTheme.typography.bodySmall)
        }
    }
}
