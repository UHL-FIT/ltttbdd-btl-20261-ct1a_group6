package com.example.nhatky

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edtUsername = findViewById<EditText>(R.id.edtRegUsername)
        val edtPassword = findViewById<EditText>(R.id.edtRegPassword)
        val edtConfirmPassword = findViewById<EditText>(R.id.edtRegConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val txtLogin = findViewById<TextView>(R.id.txtLogin)

        val database = AppDatabase.getDatabase(this)

        btnRegister.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existingUser = database.userDao().getUserByUsername(username)
                if (existingUser != null) {
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val newUser = User(username, password)
                    database.userDao().registerUser(newUser)
                    runOnUiThread {
                        Toast.makeText(this@RegisterActivity, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        txtLogin.setOnClickListener {
            finish()
        }
    }
}
