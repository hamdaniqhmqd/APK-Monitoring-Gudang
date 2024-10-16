package com.tugas.aplikasimonitoringgudang.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.databinding.ActivityRegisterBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            intentMainAct()
        }

        binding.toLogin.setOnClickListener {
            intentLoginAct()
        }
    }

    private fun intentMainAct() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun intentLoginAct() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}