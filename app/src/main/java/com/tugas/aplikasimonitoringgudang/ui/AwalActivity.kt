package com.tugas.aplikasimonitoringgudang.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.databinding.ActivityAwalBinding

class AwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAwalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAwalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}