package com.tugas.aplikasimonitoringgudang.ui

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.databinding.ActivityAwalBinding
import com.google.android.material.button.MaterialButton
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.ui.login.LoginActivity

class AwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAwalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = findViewById<MaterialButton>(R.id.btnLogin)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
