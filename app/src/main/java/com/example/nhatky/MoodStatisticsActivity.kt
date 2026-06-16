package com.example.nhatky

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MoodStatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_statistics)

        val btnBack = findViewById<ImageButton>(R.id.btnBackStats)
        val txtTotal = findViewById<TextView>(R.id.txtTotalDiaries)
        val countHappy = findViewById<TextView>(R.id.countHappy)
        val countNormal = findViewById<TextView>(R.id.countNormal)
        val countSad = findViewById<TextView>(R.id.countSad)
        val countAngry = findViewById<TextView>(R.id.countAngry)
        val txtAdvice = findViewById<TextView>(R.id.txtMoodAdvice)

        btnBack.setOnClickListener {
            finish()
        }

        val database = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val diaries = database.diaryDao().getAllEntries()
            
            val total = diaries.size
            val happy = diaries.count { it.mood == "😊" }
            val normal = diaries.count { it.mood == "😐" }
            val sad = diaries.count { it.mood == "😢" }
            val angry = diaries.count { it.mood == "😡" }

            runOnUiThread {
                txtTotal.text = "Tổng số nhật ký: $total"
                countHappy.text = happy.toString()
                countNormal.text = normal.toString()
                countSad.text = sad.toString()
                countAngry.text = angry.toString()

                txtAdvice.text = generateAdvice(happy, normal, sad, angry)
            }
        }
    }

    private fun generateAdvice(happy: Int, normal: Int, sad: Int, angry: Int): String {
        val max = maxOf(happy, normal, sad, angry)
        if (max == 0) return "Hãy bắt đầu viết nhật ký để theo dõi tâm trạng của bạn nhé!"
        
        return when {
            happy == max -> "Thật tuyệt vời! Bạn đang có một khoảng thời gian rất tích cực. Hãy tiếp tục duy trì năng lượng này nhé!"
            normal == max -> "Tâm trạng của bạn khá ổn định. Đây là trạng thái tốt để tập trung vào công việc và học tập."
            sad == max -> "Dạo này bạn có vẻ hơi buồn. Đừng ngần ngại chia sẻ với người thân hoặc dành thời gian thư giãn cho bản thân nhé."
            angry == max -> "Có vẻ bạn đang gặp nhiều áp lực. Hãy thử thiền hoặc hít thở sâu để cân bằng lại cảm xúc."
            else -> "Hãy lắng nghe cảm xúc của bản thân mỗi ngày nhé."
        }
    }
}
