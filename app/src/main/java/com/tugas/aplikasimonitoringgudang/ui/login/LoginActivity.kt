package com.tugas.aplikasimonitoringgudang.ui.login

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import com.tugas.aplikasimonitoringgudang.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var database: GudangDatabase
    private lateinit var userDao: UserDao

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = GudangDatabase.getDatabase(this)
        userDao = database.userDao()

        val usernameInput = binding.inputUser
        val passwordInput = binding.inputPass
        val loginButton = binding.btnLogin

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = userDao.getUser(username, password)
                runOnUiThread {
                    if (user != null) {
                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        // Proceed to the next activity or screen
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
