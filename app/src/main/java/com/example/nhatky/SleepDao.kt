package com.example.nhatky

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SleepDao {
    @Insert
    suspend fun insertSleep(entry: SleepEntry)

    @Query("SELECT * FROM sleep_entries ORDER BY timestamp DESC")
    suspend fun getAllSleepEntries(): List<SleepEntry>
}