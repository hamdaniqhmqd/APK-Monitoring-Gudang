package com.tugas.aplikasimonitoringgudang.ui.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.ui.login.LoginActivity

class AdminProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_profil)

        val sharedPreferences = getSharedPreferences("AdminPrefs", MODE_PRIVATE)
        val adminName = sharedPreferences.getString("adminName", getString(R.string.ini_nama_admin))
        val adminImageUri = sharedPreferences.getString("adminImageUri", null)

        val adminNameTextView = findViewById<TextView>(R.id.adminName)
        adminNameTextView.text = adminName

        val adminImageView = findViewById<ImageView>(R.id.adminImage)
        if (adminImageUri != null) {
            adminImageView.setImageURI(Uri.parse(adminImageUri))
        }

        val logoutOption = findViewById<LinearLayout>(R.id.logoutOption)
        logoutOption.setOnClickListener {
            // Clear login state
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val settingsOption = findViewById<LinearLayout>(R.id.settingsOption)
        settingsOption.setOnClickListener {
//            val intent = Intent(this, AdminProfileSettingActivity::class.java)
//            startActivity(intent)
        }
    }
}
