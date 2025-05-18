package com.example.healthmap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmap.model.Plan
import com.example.healthmap.repository.PlanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlanViewModel(application: Application) : AndroidViewModel(application) {
    private val cRepository: PlanRepository = PlanRepository(application)
    val allPlans: Flow<List<Plan>> = cRepository.allPlans
    fun insertPlan(plan: Plan) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insert(plan)
    }

    fun updatePlan(plan: Plan) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.update(plan)
    }

    fun deletePlan(plan: Plan) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.delete(plan)
    }

    fun getPlansForDate(date: LocalDate): Flow<List<Plan>> =
        cRepository.getPlansForDate(date)
}