package com.tugas.aplikasimonitoringgudang.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.databinding.ActivityLoadScreenBinding
import android.content.SharedPreferences
import android.widget.Toast
import com.tugas.aplikasimonitoringgudang.R
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.tugas.aplikasimonitoringgudang.data.session.AppPreferences
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel

class LoadScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadScreenBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.kuning)

        viewModel.sinkronisasiDataUser()

        Handler().postDelayed({
            AppPreferences.init(this)
            val userId = AppPreferences.getUserId()
            val username = AppPreferences.getUsername()
            val isLoggedIn = AppPreferences.isLoggedIn()

            val intent = if (isLoggedIn) {
                Toast.makeText(this, "Selamat Datang Kembali, $username! $userId", Toast.LENGTH_SHORT).show()
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, AwalActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, 1000)
    }
}
