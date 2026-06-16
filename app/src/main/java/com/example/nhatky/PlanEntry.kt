package com.example.nhatky

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plan_entries")
data class PlanEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
