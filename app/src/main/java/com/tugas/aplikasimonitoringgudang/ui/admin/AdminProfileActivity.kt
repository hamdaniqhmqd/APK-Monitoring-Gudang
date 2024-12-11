package com.tugas.aplikasimonitoringgudang.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.session.AppPreferences
import com.tugas.aplikasimonitoringgudang.ui.login.LoginActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel

class AdminProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    private lateinit var adminNameTextView: TextView
    private lateinit var adminImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_profil)

        adminNameTextView = findViewById(R.id.adminName)
        adminImageView = findViewById(R.id.adminImage)

        AppPreferences.init(this)
        val userId = AppPreferences.getUserId()
        val username = AppPreferences.getUsername()
        val isLoggedIn = AppPreferences.isLoggedIn()

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userId.let {
            viewModel.getUserById(it).observe(this) { user ->
                adminNameTextView.text = "Hi! ${user.adminName}"
                if (user.profileImagePath != null) {
                    val end_point = "https://gudang-pakaian-api.infitechd.my.id/storage/admin/${user.profileImagePath}"
                    Glide.with(adminImageView.context)
                        .load(end_point)
                        .placeholder(R.drawable.profile)
                        .into(adminImageView)
                } else {
                    adminImageView.setImageResource(R.drawable.profile)
                }
            }
        }

        val logoutOption = findViewById<LinearLayout>(R.id.logoutOption)
        logoutOption.setOnClickListener {
            // Clear login session
            AppPreferences.clear()

            // Redirect ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val settingsOption = findViewById<LinearLayout>(R.id.settingsOption)
        settingsOption.setOnClickListener {
            val intent = Intent(this, AdminProfileSettingActivity::class.java)
            startActivity(intent)
        }
    }
}
