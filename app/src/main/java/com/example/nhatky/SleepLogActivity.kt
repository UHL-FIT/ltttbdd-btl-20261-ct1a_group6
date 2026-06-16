package com.example.nhatky

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SleepLogActivity : AppCompatActivity() {
    private var selectedQuality: String = "Tốt 😊"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_log)

        val btnBack = findViewById<ImageButton>(R.id.btnBackSleep)
        val edtHours = findViewById<EditText>(R.id.edtSleepHours)
        val btnSave = findViewById<Button>(R.id.btnSaveSleep)

        val btnGood = findViewById<Button>(R.id.btnQualityGood)
        val btnNormal = findViewById<Button>(R.id.btnQualityNormal)
        val btnBad = findViewById<Button>(R.id.btnQualityBad)

        btnBack.setOnClickListener { finish() }

        btnGood.setOnClickListener {
            selectedQuality = "Tốt 😊"
            updateQualitySelection(btnGood, listOf(btnNormal, btnBad))
        }

        btnNormal.setOnClickListener {
            selectedQuality = "Ổn 😐"
            updateQualitySelection(btnNormal, listOf(btnGood, btnBad))
        }

        btnBad.setOnClickListener {
            selectedQuality = "Tệ 😴"
            updateQualitySelection(btnBad, listOf(btnGood, btnNormal))
        }

        val database = AppDatabase.getDatabase(this)

        btnSave.setOnClickListener {
            val hoursStr = edtHours.text.toString()
            if (hoursStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số giờ ngủ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hours = hoursStr.toFloatOrNull()
            if (hours == null || hours > 24) {
                Toast.makeText(this, "Số giờ ngủ không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val entry = SleepEntry(date = date, hours = hours, quality = selectedQuality)

            lifecycleScope.launch {
                database.sleepDao().insertSleep(entry)
                runOnUiThread {
                    Toast.makeText(this@SleepLogActivity, "Đã lưu dữ liệu giấc ngủ!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun updateQualitySelection(selected: Button, others: List<Button>) {
        selected.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#E040FB")))
        selected.setTextColor(android.graphics.Color.WHITE)
        
        others.forEach {
            it.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE))
            it.setTextColor(android.graphics.Color.parseColor("#555555"))
        }
    }
}
