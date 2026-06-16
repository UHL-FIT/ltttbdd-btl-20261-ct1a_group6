package com.example.nhatky

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class DiaryHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_history)

        val btnBack = findViewById<ImageButton>(R.id.btnBackHistory)
        val rvHistory = findViewById<RecyclerView>(R.id.rvDiaryHistory)

        btnBack.setOnClickListener {
            finish()
        }

        rvHistory.layoutManager = LinearLayoutManager(this)

        val database = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val diaries = database.diaryDao().getAllEntries()
            runOnUiThread {
                rvHistory.adapter = DiaryAdapter(diaries)
            }
        }
    }
}
