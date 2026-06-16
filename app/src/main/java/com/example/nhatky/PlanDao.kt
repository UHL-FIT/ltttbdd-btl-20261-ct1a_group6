package com.example.nhatky

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlanDao {
    @Insert
    suspend fun insertPlan(plan: PlanEntry)

    @Update
    suspend fun updatePlan(plan: PlanEntry)

    @Delete
    suspend fun deletePlan(plan: PlanEntry)

    @Query("SELECT * FROM plan_entries ORDER BY timestamp DESC")
    suspend fun getAllPlans(): List<PlanEntry>
}