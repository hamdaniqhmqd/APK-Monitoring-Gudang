package com.tugas.aplikasimonitoringgudang.ui.admin

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import java.io.File

class AdminProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    private lateinit var adminNameTextView: TextView
    private lateinit var adminImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_profil)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        adminNameTextView = findViewById(R.id.adminName)
        adminImageView = findViewById(R.id.adminImage)

        // Get the username from SharedPreferences or wherever you store it
        val sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""

        val imagePath = sharedPreferences.getString("adminImagePath", null)
        if (imagePath != null) {
            try {
                val imageFile = File(imagePath)
                if (imageFile.exists()) {
                    val imageUri = Uri.fromFile(imageFile)
                    adminImageView.setImageURI(imageUri)
                } else {
                    Log.e("AdminProfileActivity", "Image file does not exist")
                    Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AdminProfileActivity", "Error setting image from path", e)
                Toast.makeText(this, "Error loading profile image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("AdminProfileActivity", "Image path is null")
        }

        try {
            viewModel.getAdminLiveData(username).observe(this) { admin ->
                admin?.let {
                    adminNameTextView.text = it.adminName
                    if (it.profileImagePath != null) {
                        val imageUri = Uri.fromFile(File(it.profileImagePath))
                        adminImageView.setImageURI(imageUri)
                    }
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
            val intent = Intent(this, AdminProfileSettingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfileImage()
        loadAdminName()
    }

    private fun loadProfileImage() {
        val sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val imagePath = sharedPreferences.getString("adminImagePath", null)
        if (imagePath != null) {
            try {
                val imageFile = File(imagePath)
                if (imageFile.exists()) {
                    val imageUri = Uri.fromFile(imageFile)
                    adminImageView.setImageURI(imageUri)
                } else {
                    Log.e("AdminProfileActivity", "Image file does not exist")
                    Toast.makeText(this, "Image file not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AdminProfileActivity", "Error setting image from path", e)
                Toast.makeText(this, "Error loading profile image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("AdminProfileActivity", "Image path is null")
        }
    }

    private fun loadAdminName() {
        val sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") ?: ""
        
        viewModel.getAdminName(username).observe(this) { adminName ->
            val displayName = if (adminName.isNullOrEmpty()) "Admin" else adminName
            adminNameTextView.text = "Hi! $displayName"
        }
    }
}
