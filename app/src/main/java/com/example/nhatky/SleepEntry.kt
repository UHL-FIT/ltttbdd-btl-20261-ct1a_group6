package com.example.nhatky

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_entries")
data class SleepEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val hours: Float,
    val quality: String,
    val timestamp: Long = System.currentTimeMillis()
)
