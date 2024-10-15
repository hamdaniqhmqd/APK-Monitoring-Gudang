package com.tugas.aplikasimonitoringgudang.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.AwalActivity
import com.tugas.aplikasimonitoringgudang.databinding.ActivityLoadScreenBinding

class LoadScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, AwalActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}