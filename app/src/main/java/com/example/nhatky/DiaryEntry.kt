package com.example.nhatky

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val mood: String,
    val timestamp: Long = System.currentTimeMillis()
)
