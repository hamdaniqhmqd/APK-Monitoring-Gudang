package com.tugas.aplikasimonitoringgudang.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.databinding.ActivityRegisterBinding
import com.tugas.aplikasimonitoringgudang.ui.login.LoginActivity
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.local.UserDao
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModelUser: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val username = binding.inputUser.text.toString()
            val password = binding.inputPass.text.toString()
            val repeatPassword = binding.inputRepeatPass.text.toString()

            if (password == repeatPassword) {
//                CoroutineScope(Dispatchers.IO).launch {
//                    userDao.insert(User(username = username, password = password, adminName = username))
//                    intentLoginAct()
//                }
                viewModelUser.insert(User(username = username, password = password, adminName = username))
                Toast.makeText(this, "Register successful", Toast.LENGTH_SHORT).show()
                intentLoginAct()
            } else {
                Toast.makeText(this, "Passwords tidak sama", Toast.LENGTH_SHORT).show()
            }
        }

        binding.toLogin.setOnClickListener {
            intentLoginAct()
        }
    }

    private fun intentLoginAct() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
