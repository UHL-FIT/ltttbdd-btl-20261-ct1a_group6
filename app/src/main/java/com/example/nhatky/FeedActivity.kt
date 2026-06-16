package com.example.nhatky

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class FeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val btnBack = findViewById<ImageButton>(R.id.btnBackFeed)
        btnBack.setOnClickListener {
            finish()
        }
    }
}
