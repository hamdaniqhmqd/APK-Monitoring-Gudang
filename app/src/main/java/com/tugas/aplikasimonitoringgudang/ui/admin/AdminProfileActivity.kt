package com.tugas.aplikasimonitoringgudang.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.ui.login.LoginActivity

class AdminProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_profil)

        val logoutOption = findViewById<LinearLayout>(R.id.logoutOption)
        logoutOption.setOnClickListener {
            // Clear login state
            val sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
