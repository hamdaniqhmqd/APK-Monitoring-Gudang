package com.tugas.aplikasimonitoringgudang.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.data.user.UserDao
import com.tugas.aplikasimonitoringgudang.databinding.ActivityLoginBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.ui.register.RegisterActivity
import com.tugas.aplikasimonitoringgudang.ui.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var database: GudangDatabase
    private lateinit var userDao: UserDao

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = GudangDatabase.getDatabase(this)
        userDao = database.userDao()

        val usernameInput = binding.inputUser
        val passwordInput = binding.inputPass

        // Retrieve the credentials from the intent
        val username = intent.getStringExtra("username") ?: ""
        val password = intent.getStringExtra("password") ?: ""

        // Pre-fill the login form
        usernameInput.setText(username)
        passwordInput.setText(password)

        binding.btnLogin.setOnClickListener {
            val inputUsername = usernameInput.text.toString()
            val inputPassword = passwordInput.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = userDao.getUser(inputUsername, inputPassword)

                runOnUiThread {
                    if (user != null) {
                        // Save login state
                        viewModel.update(User(id = user.id, username = inputUsername, password = inputPassword, statusLoging = 1))

                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        intentMainAct()

                        // In LoginActivity, after successful login
                        val sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("username", inputUsername).apply()

                        val sharedPreferencesLogin = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                        sharedPreferencesLogin.edit().putBoolean("isLoggedIn", true).apply()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.toRegister.setOnClickListener {
            intentRegisterAct()
        }
    }

    private fun intentMainAct() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun intentRegisterAct() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
