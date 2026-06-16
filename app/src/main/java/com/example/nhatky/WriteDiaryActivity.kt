package com.example.nhatky

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WriteDiaryActivity : AppCompatActivity() {
    private var selectedMood: String = "😊"
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_diary)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnSave = findViewById<Button>(R.id.btnSaveDiary)
        val edtTitle = findViewById<EditText>(R.id.edtDiaryTitle)
        val edtContent = findViewById<EditText>(R.id.edtDiaryContent)
        val txtDate = findViewById<TextView>(R.id.txtDiaryDate)
        val txtTime = findViewById<TextView>(R.id.txtDiaryTime)
        val layoutDateTime = findViewById<android.view.View>(R.id.layoutDateTime)

        val moodHappy = findViewById<Button>(R.id.moodHappy)
        val moodNormal = findViewById<Button>(R.id.moodNormal)
        val moodSad = findViewById<Button>(R.id.moodSad)
        val moodAngry = findViewById<Button>(R.id.moodAngry)

        // Hiển thị ngày giờ hiện tại mặc định
        updateDateTimeDisplay(txtDate, txtTime)

        btnBack.setOnClickListener {
            finish()
        }

        layoutDateTime.setOnClickListener {
            showDatePicker(txtDate, txtTime)
        }

        moodHappy.setOnClickListener { selectedMood = "😊"; updateMoodSelection(moodHappy) }
        moodNormal.setOnClickListener { selectedMood = "😐"; updateMoodSelection(moodNormal) }
        moodSad.setOnClickListener { selectedMood = "😢"; updateMoodSelection(moodSad) }
        moodAngry.setOnClickListener { selectedMood = "😡"; updateMoodSelection(moodAngry) }

        btnSave.setOnClickListener {
            val title = edtTitle.text.toString().trim()
            val content = edtContent.text.toString().trim()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tiêu đề và nội dung", Toast.LENGTH_SHORT).show()
            } else {
                val diaryEntry = DiaryEntry(
                    title = title, 
                    content = content, 
                    mood = selectedMood,
                    timestamp = calendar.timeInMillis
                )
                val database = AppDatabase.getDatabase(this)
                
                lifecycleScope.launch {
                    database.diaryDao().insert(diaryEntry)
                    runOnUiThread {
                         Toast.makeText(this@WriteDiaryActivity, "Đã lưu nhật ký tâm trạng $selectedMood", Toast.LENGTH_LONG).show()
                         finish()
                    }
                }
            }
        }
    }

    private fun updateDateTimeDisplay(txtDate: TextView, txtTime: TextView) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        txtDate.text = dateFormat.format(calendar.time)
        txtTime.text = timeFormat.format(calendar.time)
    }

    private fun showDatePicker(txtDate: TextView, txtTime: TextView) {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showTimePicker(txtDate, txtTime)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(txtDate: TextView, txtTime: TextView) {
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateDateTimeDisplay(txtDate, txtTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun updateMoodSelection(selectedBtn: Button) {
        val buttons = listOf(
            findViewById<Button>(R.id.moodHappy),
            findViewById<Button>(R.id.moodNormal),
            findViewById<Button>(R.id.moodSad),
            findViewById<Button>(R.id.moodAngry)
        )
        buttons.forEach { it.alpha = 0.5f }
        selectedBtn.alpha = 1.0f
    }
}
