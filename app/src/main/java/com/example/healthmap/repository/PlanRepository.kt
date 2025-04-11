package com.example.healthmap.repository

import com.example.healthmap.model.Plan
import java.time.LocalDate


object PlanRepository {
    private val allPlans = listOf(
        Plan("Alex", "Running", "06:30", LocalDate.of(2024, 6, 10), 145.130, -37.917),
        Plan("Mia", "Walking", "07:15", LocalDate.of(2024, 6, 10), 145.131, -37.916),
        Plan("Leo", "Gym", "08:00", LocalDate.of(2024, 6, 10), 145.132, -37.915),
        Plan("Tom", "Swimming", "09:00", LocalDate.of(2024, 6, 10), 145.133, -37.914),
        Plan("Lily", "Yoga", "10:00", LocalDate.of(2024, 6, 10), 145.134, -37.913),
        Plan("John", "Cycling", "11:00", LocalDate.of(2024, 6, 10), 145.135, -37.912),
        Plan("Amy", "Meditation", "12:00", LocalDate.of(2024, 6, 10), 145.136, -37.911),
        Plan("Jack", "Jogging", "13:00", LocalDate.of(2024, 6, 10), 145.137, -37.910),
        Plan("Nina", "Dancing", "14:00", LocalDate.of(2024, 6, 10), 145.138, -37.909),
        Plan("Ben", "Boxing", "15:00", LocalDate.of(2024, 6, 10), 145.139, -37.908),
        Plan("Emily", "Pilates", "16:00", LocalDate.of(2024, 6, 10), 145.140, -37.907),
        Plan("Chris", "Stretching", "17:00", LocalDate.of(2024, 6, 10), 145.141, -37.906),
        Plan("Grace", "Cardio", "18:00", LocalDate.of(2024, 6, 10), 145.142, -37.905),
        Plan("Ethan", "Rowing", "19:00", LocalDate.of(2024, 6, 10), 145.143, -37.904),
        Plan("Sophie", "CrossFit", "20:00", LocalDate.of(2024, 6, 10), 145.144, -37.903),

        Plan("Alex", "Running", "06:30", LocalDate.of(2024, 6, 11), 145.130, -37.917),
        Plan("Mia", "Walking", "07:15", LocalDate.of(2024, 6, 11), 145.131, -37.916),
        Plan("Leo", "Gym", "08:00", LocalDate.of(2024, 6, 11), 145.132, -37.915),
        Plan("Tom", "Swimming", "09:00", LocalDate.of(2024, 6, 11), 145.133, -37.914),
        Plan("Lily", "Yoga", "10:00", LocalDate.of(2024, 6, 11), 145.134, -37.913),
        Plan("John", "Cycling", "11:00", LocalDate.of(2024, 6, 11), 145.135, -37.912),
        Plan("Amy", "Meditation", "12:00", LocalDate.of(2024, 6, 11), 145.136, -37.911),
        Plan("Jack", "Jogging", "13:00", LocalDate.of(2024, 6, 11), 145.137, -37.910),
        Plan("Nina", "Dancing", "14:00", LocalDate.of(2024, 6, 11), 145.138, -37.909),
        Plan("Ben", "Boxing", "15:00", LocalDate.of(2024, 6, 11), 145.139, -37.908),
        Plan("Emily", "Pilates", "16:00", LocalDate.of(2024, 6, 11), 145.140, -37.907),
        Plan("Chris", "Stretching", "17:00", LocalDate.of(2024, 6, 11), 145.141, -37.906),
        Plan("Grace", "Cardio", "18:00", LocalDate.of(2024, 6, 11), 145.142, -37.905),
        Plan("Ethan", "Rowing", "19:00", LocalDate.of(2024, 6, 11), 145.143, -37.904),
        Plan("Sophie", "CrossFit", "20:00", LocalDate.of(2024, 6, 11), 145.144, -37.903)
    )

    fun getPlansForDate(date: LocalDate): List<Plan> {
        return allPlans.filter { it.date == date }
    }
}
