package com.example.healthmap.model
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Entity
data class Plan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val activity: String,
    val time: String,
    val date: LocalDate,
    val lng: Double,
    val lat: Double
)

@Dao
interface PlanDAO {
    @Query("SELECT * FROM `Plan`")
    fun getAllPlans(): Flow<List<Plan>>

    @Query("SELECT * FROM `Plan` WHERE date = :date")
    fun getPlansForDate(date: LocalDate): Flow<List<Plan>>

    @Insert
    fun insertPlan(plan: Plan)

    @Update
    fun updatePlan(plan: Plan)

    @Delete
    fun deletePlan(plan: Plan)
}