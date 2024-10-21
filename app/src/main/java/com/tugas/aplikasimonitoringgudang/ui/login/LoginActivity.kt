package com.tugas.aplikasimonitoringgudang.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase // Ensure this import is correct
import com.tugas.aplikasimonitoringgudang.data.user.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.button.MaterialButton
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.databinding.ActivityLoginBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.ui.register.RegisterActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var database: GudangDatabase
    private lateinit var userDao: UserDao

    private lateinit var binding: ActivityLoginBinding

    private val userLoginViewModel: UserViewModel by viewModels()

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
                        userLoginViewModel.update(User(id = user.id, username = inputUsername, password = inputPassword, statusLoging = 1))

                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        intentMainAct(inputUsername)
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

    private fun intentMainAct(userName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", userName)
        startActivity(intent)
    }

    private fun intentRegisterAct() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
