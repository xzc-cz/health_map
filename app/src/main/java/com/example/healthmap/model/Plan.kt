package com.example.healthmap.model
import java.time.LocalDate

data class Plan(
    val name: String,
    val activity: String,
    val time: String,
    val date: LocalDate,
    val lng: Double,
    val lat: Double
)
