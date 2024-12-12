package com.tugas.aplikasimonitoringgudang.ui

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tugas.aplikasimonitoringgudang.databinding.ActivityAwalBinding
import com.google.android.material.button.MaterialButton
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.ui.login.LoginActivity
import com.tugas.aplikasimonitoringgudang.ui.register.RegisterActivity

class AwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAwalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.kuning)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
