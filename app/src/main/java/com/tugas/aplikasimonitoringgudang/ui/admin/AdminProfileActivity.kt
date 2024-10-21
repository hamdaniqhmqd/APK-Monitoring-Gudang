package com.tugas.aplikasimonitoringgudang.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.ui.login.LoginActivity
import com.tugas.aplikasimonitoringgudang.ui.user.UserViewModel

class AdminProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_profil)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val adminNameTextView = findViewById<TextView>(R.id.adminName)
        val adminImageView = findViewById<ImageView>(R.id.adminImage)

        // Get the username from SharedPreferences or wherever you store it
        val sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""

        try {
            viewModel.getAdminLiveData(username).observe(this) { admin ->
                admin?.let {
                    adminNameTextView.text = it.username
                    // Update other UI components as needed
                }
            }
        } catch (e: Exception) {
            Log.e("AdminProfileActivity", "Error observing admin data", e)
            Toast.makeText(this, "Error loading admin profile", Toast.LENGTH_SHORT).show()
        }

        val logoutOption = findViewById<LinearLayout>(R.id.logoutOption)
        logoutOption.setOnClickListener {
            // Clear login state
            val sharedPreferencesLogin = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
            sharedPreferencesLogin.edit().clear().apply()

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
