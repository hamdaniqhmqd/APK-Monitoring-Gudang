package com.tugas.aplikasimonitoringgudang.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
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

        viewModel.getAdminLiveData("adminUsername").observe(this, { admin ->
            admin?.let {
                adminNameTextView.text = it.username
                // Update other UI components as needed
            }
        })

        val logoutOption = findViewById<LinearLayout>(R.id.logoutOption)
        logoutOption.setOnClickListener {
            // Clear login state
            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
