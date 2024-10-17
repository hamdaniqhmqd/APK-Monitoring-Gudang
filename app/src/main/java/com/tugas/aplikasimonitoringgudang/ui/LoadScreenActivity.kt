package com.tugas.aplikasimonitoringgudang.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.databinding.ActivityLoadScreenBinding
import android.content.SharedPreferences

class LoadScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        val intent = if (isLoggedIn) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, AwalActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
