package com.example.healthmap.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmap.model.Plan
import com.example.healthmap.model.PlanDto
import com.example.healthmap.repository.PlanRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class PlanViewModel(application: Application) : AndroidViewModel(application) {
    private val firestore = FirebaseFirestore.getInstance()
    private val cRepository: PlanRepository = PlanRepository(application)
    val allPlans: Flow<List<Plan>> = cRepository.allPlans
    fun getPlansForDate(date: LocalDate): Flow<List<Plan>> =
        cRepository.getPlansForDate(date)

    fun getCount() = cRepository.getCount()


    fun insertPlan(plan: Plan) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insert(plan)
        try {
            val ref = firestore.collection("plans")
                .document(plan.id.toString())
                .set(plan2Doc(plan))
                .await()
            Log.d("Plan", "Inserted One Plan ID=${plan.id}")
        } catch (e: Exception) {
            Log.e("Plan", "Insert Failed", e)
        }
    }

    fun insertManyPlans(plans: List<Plan>) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insertManyPlans(plans)
    }

    fun updatePlan(plan: Plan) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.update(plan)
        try {
            firestore.collection("plans")
                .document(plan.id.toString())
                .set(plan2Doc(plan))
                .await()
            Log.d("Plan", "Updated One Plan ID=${plan.id}")
        } catch (e: Exception) {
            Log.e("Plan", "Update Failed", e)
        }
    }

    fun deletePlan(plan: Plan) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.delete(plan)
        try {
            firestore.collection("plans")
                .document(plan.id.toString())
                .delete()
                .await()
            Log.d("Plan", "Deleted Document ${plan.id}")
        } catch (e: Exception) {
            Log.e("Plan", "Delete Failed", e)
        }
    }

    fun clearAll() = viewModelScope.launch(Dispatchers.IO) {
        cRepository.clearAll()
    }

    fun updateAllFromCloud() = viewModelScope.launch(Dispatchers.IO) {
        cRepository.clearAll()
        val snapshot = firestore.collection("plans")
            .get()
            .await()
        val plans = snapshot.documents.mapNotNull { doc ->
           val dto = doc.toObject(PlanDto::class.java) ?: PlanDto()
            doc2Plan(dto)
        }
        cRepository.insertManyPlans(plans)
    }

    fun checkPlan(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val exists = cRepository.hasPlan(id)
            onResult(exists)
        }
    }
}

fun plan2Doc(plan: Plan): PlanDto {
    return PlanDto(
        id = plan.id,
        name = plan.name,
        activity = plan.activity,
        time = plan.time,
        date = plan.date.toString(),
        lat = plan.lat,
        lng = plan.lng
    )
}

fun doc2Plan(planDto: PlanDto): Plan {
    return Plan(
        id = planDto.id,
        name = planDto.name,
        activity = planDto.activity,
        time = planDto.time,
        date = planDto.date.let { LocalDate.parse(it) },
        lng = planDto.lng,
        lat = planDto.lat
    )
}