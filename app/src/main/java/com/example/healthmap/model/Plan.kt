package com.example.healthmap.model
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class PlanDto(
    val id: Int = 0,
    val name: String = "",
    val activity: String = "",
    val time: String = "",
    val date: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

@Entity
data class Plan(
    @PrimaryKey(autoGenerate = false)
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
    @Query("SELECT COUNT(*) FROM `Plan`")
    fun getCount(): Flow<Int>

    @Query("SELECT * FROM `Plan`")
    fun getAllPlans(): Flow<List<Plan>>

    @Query("SELECT EXISTS(SELECT 1 FROM `Plan` WHERE id = :id)")
    suspend fun existsById(id: Int): Boolean

    @Query("SELECT * FROM `Plan` WHERE date = :date")
    fun getPlansForDate(date: LocalDate): Flow<List<Plan>>

    @Insert
    fun insertPlan(plan: Plan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertManyPlans(plans: List<Plan>)

    @Update
    fun updatePlan(plan: Plan)

    @Delete
    fun deletePlan(plan: Plan)

    @Query("DELETE FROM `Plan`")
    suspend fun clearAll()
}