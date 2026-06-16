package com.example.nhatky

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
@Suppress("SpellCheckingInspection", "Lint")
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // ==========================================
        // 1. ÁNH XẠ CÁC PHẦN TỬ GIAO DIỆN (WIDGETS)
        // ==========================================
        setupViews()
        setupListeners()
    }

    private lateinit var txtMoodStatus: TextView
    private lateinit var txtScore: TextView
    private lateinit var txtScoreStatus: TextView
    private lateinit var txtMindfulHours: TextView
    private lateinit var txtSleepInfo: TextView
    private lateinit var database: AppDatabase

    private fun setupViews() {
        txtMoodStatus = findViewById(R.id.txtMoodStatus)
        txtScore = findViewById(R.id.txtScore)
        txtScoreStatus = findViewById(R.id.txtScoreStatus)
        txtMindfulHours = findViewById(R.id.txtMindfulHours)
        txtSleepInfo = findViewById(R.id.txtSleepInfo)
        database = AppDatabase.getDatabase(this)
    }

    override fun onResume() {
        super.onResume()
        updateUIFromDatabase()
    }

    private fun updateUIFromDatabase() {
        lifecycleScope.launch {
            val diaries = database.diaryDao().getAllEntries()
            val sleepEntries = database.sleepDao().getAllSleepEntries()

            if (diaries.isNotEmpty()) {
                val latest = diaries.first()
                runOnUiThread {
                    txtMoodStatus.text = when (latest.mood) {
                        "😊" -> "Vui vẻ"
                        "😐" -> "Bình thường"
                        "😢" -> "Hơi buồn"
                        "😡" -> "Tức giận"
                        else -> "Ổn định"
                    } + " " + latest.mood
                }
            } else {
                runOnUiThread {
                    txtMoodStatus.text = "Chưa có dữ liệu"
                }
            }

            if (sleepEntries.isNotEmpty()) {
                val latestSleep = sleepEntries.first()
                runOnUiThread {
                    txtSleepInfo.text = "${latestSleep.hours} giờ (${latestSleep.quality})"
                }
            }

            // Cập nhật biểu đồ tâm trạng đơn giản
            val last5Diaries = diaries.take(5).reversed()
            runOnUiThread {
                val bars = listOf(
                    findViewById<View>(R.id.bar1),
                    findViewById<View>(R.id.bar2),
                    findViewById<View>(R.id.bar3),
                    findViewById<View>(R.id.bar4),
                    findViewById<View>(R.id.bar5)
                )
                
                bars.forEachIndexed { index, view ->
                    if (index < last5Diaries.size) {
                        val diary = last5Diaries[index]
                        val heightDp = when (diary.mood) {
                            "😊" -> 45
                            "😐" -> 30
                            "😢" -> 20
                            "😡" -> 15
                            else -> 25
                        }
                        val params = view.layoutParams
                        params.height = (heightDp * resources.displayMetrics.density).toInt()
                        view.layoutParams = params
                        view.visibility = View.VISIBLE
                    } else {
                        view.visibility = View.INVISIBLE
                    }
                }
            }

            // Giả lập điểm bản tin dựa trên số lượng nhật ký
            val score = if (diaries.isEmpty()) 0 else (70 + (diaries.size * 2)).coerceAtMost(100)
            val scoreStatus = when {
                score >= 80 -> "Rất tốt"
                score >= 60 -> "Khỏe mạnh"
                score >= 40 -> "Trung bình"
                else -> "Cần cải thiện"
            }
            runOnUiThread {
                txtScore.text = score.toString()
                txtScoreStatus.text = scoreStatus
            }
        }
    }

    private fun setupListeners() {
        val btnNotification = findViewById<ImageButton>(R.id.btnNotification)
        val txtViewAllAction = findViewById<TextView>(R.id.txtViewAllAction)
        val txtViewAllHistory = findViewById<TextView>(R.id.txtViewAllHistory)
        val txtViewAllHours = findViewById<TextView>(R.id.txtViewAllHours)
        val btnMoreOptions = findViewById<ImageButton>(R.id.btnMoreOptions)

        val tabActivity = findViewById<Button>(R.id.tabActivity)
        val tabMood = findViewById<Button>(R.id.tabMood)
        val tabFeed = findViewById<Button>(R.id.tabFeed)
        val tabSleep = findViewById<Button>(R.id.tabSleep)
        val fabWriteDiary = findViewById<FloatingActionButton>(R.id.fabWriteDiary)

        val btnMon = findViewById<Button>(R.id.btnMon)
        val btnTue = findViewById<Button>(R.id.btnTue)
        val btnWed = findViewById<Button>(R.id.btnWed)
        val btnThu = findViewById<Button>(R.id.btnThu)
        val btnFri = findViewById<Button>(R.id.btnFri)

        val cardMood = findViewById<androidx.cardview.widget.CardView>(R.id.cardMood)
        val cardScore = findViewById<androidx.cardview.widget.CardView>(R.id.cardScore)

        cardMood?.setOnClickListener {
            val intent = Intent(this, MoodStatisticsActivity::class.java)
            startActivity(intent)
        }

        cardScore?.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
        }

        tabActivity?.setOnClickListener {
            val intent = Intent(this, PlansActivity::class.java)
            startActivity(intent)
        }
        txtViewAllAction?.setOnClickListener {
            val intent = Intent(this, PlansActivity::class.java)
            startActivity(intent)
        }
        fabWriteDiary?.setOnClickListener {
            val intent = Intent(this, WriteDiaryActivity::class.java)
            startActivity(intent)
        }
        tabMood?.setOnClickListener {
            val intent = Intent(this, MoodStatisticsActivity::class.java)
            startActivity(intent)
        }
        tabFeed?.setOnClickListener {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
        }
        tabSleep?.setOnClickListener {
            val intent = Intent(this, SleepLogActivity::class.java)
            startActivity(intent)
        }

        btnMon?.setOnClickListener {
            txtMoodStatus.text = "Tức giận 😡"
            txtScore.text = "40"
            Toast.makeText(this, "Thứ hai: Trạng thái căng thẳng", Toast.LENGTH_SHORT).show()
        }

        btnTue?.setOnClickListener {
            txtMoodStatus.text = "Lo lắng 😰"
            txtScore.text = "55"
            Toast.makeText(this, "Thứ ba: Áp lực nhẹ", Toast.LENGTH_SHORT).show()
        }

        btnWed?.setOnClickListener {
            txtMoodStatus.text = "Mệt mỏi 😷"
            txtScore.text = "62"
            Toast.makeText(this, "Thứ tư: Cơ thể cần nghỉ ngơi", Toast.LENGTH_SHORT).show()
        }

        btnThu?.setOnClickListener {
            txtMoodStatus.text = "Ổn định 😬"
            txtScore.text = "75"
            Toast.makeText(this, "Thứ năm: Tâm trạng bình thường", Toast.LENGTH_SHORT).show()
        }

        btnFri?.setOnClickListener {
            txtMoodStatus.text = "Vui vẻ 😁"
            txtScore.text = "88"
            txtMindfulHours.text = "4 Giờ"
            Toast.makeText(this, "Thứ sáu: Tuyệt vời, sẵn sàng đón cuối tuần!", Toast.LENGTH_SHORT).show()
        }

        btnNotification?.setOnClickListener {
            Toast.makeText(this, "Bạn không có thông báo mới", Toast.LENGTH_SHORT).show()
        }

        txtViewAllAction?.setOnClickListener {
            val intent = Intent(this, PlansActivity::class.java)
            startActivity(intent)
        }

        txtViewAllHistory?.setOnClickListener {
            val intent = Intent(this, DiaryHistoryActivity::class.java)
            startActivity(intent)
        }

        txtViewAllHours?.setOnClickListener {
            Toast.makeText(this, "Mở báo cáo chi tiết Giờ Tâm Thức", Toast.LENGTH_SHORT).show()
        }

        btnMoreOptions?.setOnClickListener {
            Toast.makeText(this, "Mở menu cài đặt bổ sung", Toast.LENGTH_SHORT).show()
        }
    }
}