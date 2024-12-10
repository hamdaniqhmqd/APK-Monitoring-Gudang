package com.tugas.aplikasimonitoringgudang.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.session.AppPreferences
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.local.UserDao
import com.tugas.aplikasimonitoringgudang.databinding.ActivityLoginBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.ui.register.RegisterActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
       private lateinit var binding: ActivityLoginBinding

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.init(this)

        binding.btnLogin.setOnClickListener {
            val inputUsername = binding.inputUser.text.toString()
            val inputPassword = binding.inputPass.text.toString()

            val user = User(
                id = 0,
                username = inputUsername,
                password = inputPassword,
                adminName = "",
                profileImagePath = ""
            )

            // Observasi hasil login
            viewModel.login(user).observe(this) { loggedInUser ->
                if (loggedInUser != null) {
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()

                    // Simpan data pengguna ke SharedPreferences
                    AppPreferences.setUserId(loggedInUser.id)
                    AppPreferences.setUsername(loggedInUser.username)
                    AppPreferences.setLoggedIn(true)

                    intentMainAct()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }


//            CoroutineScope(Dispatchers.IO).launch {
//                val user = userDao.getUser(inputUsername, inputPassword)
//
//                runOnUiThread {
//                    if (user != null) {
//                        // Save login state
//                        viewModel.update(User(id = user.id, username = inputUsername, password = inputPassword, adminName = inputUsername))
//
//                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
//
//                        AppPreferences.setUserId(user.id)
//                        AppPreferences.setUsername(inputUsername)
//                        AppPreferences.setLoggedIn(true)
//
//                        intentMainAct()
//                    } else {
//                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }

        binding.toRegister.setOnClickListener {
            intentRegisterAct()
        }
    }

    private fun intentMainAct() {
        val intent = Intent(this, MainActivity::class.java)
//        intent.putExtra("user_id", id)
//        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun intentRegisterAct() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
