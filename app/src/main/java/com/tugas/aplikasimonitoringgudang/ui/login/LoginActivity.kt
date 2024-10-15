package com.tugas.aplikasimonitoringgudang.ui.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tugas.aplikasimonitoringgudang.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var database: GudangDatabase
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = GudangDatabase.getDatabase(this)
        userDao = database.userDao()

        val usernameInput = findViewById<EditText>(R.id.inputUser)
        val passwordInput = findViewById<EditText>(R.id.inputPass)
        val loginButton = findViewById<MaterialButton>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = User(username = username, password = password)
                userDao.insert(user)
            }
        }
    }
}
