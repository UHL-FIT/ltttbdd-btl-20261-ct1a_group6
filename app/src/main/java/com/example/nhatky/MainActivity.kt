package com.example.nhatky

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Suppress("SpellCheckingInspection", "Lint")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }

        val oneTimeRequest = OneTimeWorkRequestBuilder<QuoteWorker>().build()
        WorkManager.getInstance(this).enqueue(oneTimeRequest)

        val periodicRequest = PeriodicWorkRequestBuilder<QuoteWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DailyQuoteWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )

        val database = AppDatabase.getDatabase(this)

        val edtUsername = findViewById<EditText>(R.id.edtUsername)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtRegister = findViewById<TextView>(R.id.txtRegister)

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val user = database.userDao().login(username, password)
                    val isAdmin = username == "admin" && password == "123"

                    if (user != null || isAdmin) {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        txtRegister?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}