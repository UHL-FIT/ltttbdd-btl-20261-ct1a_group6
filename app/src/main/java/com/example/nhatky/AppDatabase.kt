package com.example.nhatky

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DiaryEntry::class, User::class, SleepEntry::class, PlanEntry::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
    abstract fun userDao(): UserDao
    abstract fun sleepDao(): SleepDao
    abstract fun planDao(): PlanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "diary_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
