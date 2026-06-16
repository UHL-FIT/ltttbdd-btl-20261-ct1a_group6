package com.example.nhatky

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DiaryDao {
    @Insert
    suspend fun insert(entry: DiaryEntry)

    @Query("SELECT * FROM diary_entries ORDER BY timestamp DESC")
    suspend fun getAllEntries(): List<DiaryEntry>
}