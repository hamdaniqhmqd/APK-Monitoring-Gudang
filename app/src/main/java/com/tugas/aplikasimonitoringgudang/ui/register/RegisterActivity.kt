package com.tugas.aplikasimonitoringgudang.ui.register

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

        window.statusBarColor = ContextCompat.getColor(this, R.color.kuning)

        val networkHelper = NetworkHelper(this)

        binding.btnRegister.setOnClickListener {
            if (networkHelper.isConnected()) {
                proses_register()
            } else {
                AlertConnect()
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

    private fun proses_register() {
        val username = binding.inputUser.text.toString()
        val password = binding.inputPass.text.toString()
        val repeatPassword = binding.inputRepeatPass.text.toString()
        val id_user: Long = System.currentTimeMillis()

        if (password == repeatPassword) {
            viewModelUser.insert(User(id = id_user, username = username, password = password, adminName = username))
            Toast.makeText(this, "Register successful", Toast.LENGTH_SHORT).show()
            intentLoginAct()
        } else {
            Toast.makeText(this, "Passwords tidak sama", Toast.LENGTH_SHORT).show()
        }
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
            proses_register()
        }

        // Tampilkan AlertDialog
        alertDialog.show()
    }
}
