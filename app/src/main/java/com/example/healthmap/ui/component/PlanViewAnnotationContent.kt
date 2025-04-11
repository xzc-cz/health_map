package com.example.healthmap.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthmap.model.Plan

@Composable
fun PlanViewAnnotationContent(plan: Plan) {
    Text(
        text = "📍 ${plan.name}: ${plan.activity} @ ${plan.time}",
        modifier = Modifier
            .padding(4.dp)
            .background(Color.White)
            .border(1.dp, Color.Black)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        fontSize = 14.sp
    )
}
