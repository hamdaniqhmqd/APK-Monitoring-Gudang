package com.tugas.aplikasimonitoringgudang

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tugas.aplikasimonitoringgudang.databinding.ActivityAwalBinding

class AwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAwalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAwalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}