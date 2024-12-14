package com.tugas.aplikasimonitoringgudang.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
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

        window.statusBarColor = ContextCompat.getColor(this, R.color.kuning)

        val networkHelper = NetworkHelper(this)
        AppPreferences.init(this)

        binding.btnLogin.setOnClickListener {
            if (networkHelper.isConnected()) {
                proses_login()
            } else {
                AlertConnect()
            }
        }


        binding.toRegister.setOnClickListener {
            intentRegisterAct()
        }
    }

    private fun proses_login() {
        val inputUsername = binding.inputUser.text.toString()
        val inputPassword = binding.inputPass.text.toString()

        if (inputUsername.isBlank() || inputPassword.isBlank()) {
            Toast.makeText(this, "kolom username dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }

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
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                // Simpan data pengguna ke SharedPreferences
                AppPreferences.setUserId(loggedInUser.id)
                AppPreferences.setUsername(loggedInUser.username)
                AppPreferences.setLoggedIn(true)

                // Lanjutkan ke halaman utama
                intentMainAct()
            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
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

    private fun AlertConnect() {
        // Inflate custom layout
        val dialogView = layoutInflater.inflate(R.layout.notifikasi_alert_dialog, null)

        // Bangun AlertDialog
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView) // Gunakan layout kustom
            .setCancelable(false) // Tidak bisa ditutup kecuali klik tombol
            .create()

        // Atur aksi untuk tombol
        dialogView.findViewById<Button>(R.id.btn_muat_ulang).setOnClickListener {
            // Reload Activity untuk cek ulang koneksi
            recreate()
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_ya).setOnClickListener {
            // lanjut ke act atau fragmnet yang dipilih
            proses_login()
        }

        // Tampilkan AlertDialog
        alertDialog.show()
    }
}
