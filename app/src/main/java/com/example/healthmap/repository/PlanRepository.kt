package com.example.healthmap.repository

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.healthmap.model.Plan
import com.example.healthmap.model.PlanDAO
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


object Converters {
    @TypeConverter
    @JvmStatic
    fun fromLocalDate(date: LocalDate?): String? =
        date?.toString()

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it) }
}

@Database(entities = [Plan::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PlanDatabase : RoomDatabase() {
    abstract fun planDAO(): PlanDAO

    companion object {
        @Volatile
        private var INSTANCE: PlanDatabase? = null
        fun getDatabase(context: Context): PlanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlanDatabase::class.java,
                    "plan_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class PlanRepository(application: Application) {
    private var planDao: PlanDAO =
        PlanDatabase.getDatabase(application).planDAO()
    val allPlans: Flow<List<Plan>> = planDao.getAllPlans()
    suspend fun hasPlan(id: Int): Boolean = planDao.existsById(id)
    fun getPlansForDate(date: LocalDate): Flow<List<Plan>> = planDao.getPlansForDate(date)

    fun getCount() = planDao.getCount()

    suspend fun insert(plan: Plan) {
        planDao.insertPlan(plan)
    }

    suspend fun insertManyPlans(plans: List<Plan>) {
        planDao.insertManyPlans(plans)
    }

    suspend fun delete(plan: Plan) {
        planDao.deletePlan(plan)
    }

    suspend fun update(plan: Plan) {
        planDao.updatePlan(plan)
    }

    suspend fun clearAll() = planDao.clearAll()
}
