package com.tugas.aplikasimonitoringgudang.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.databinding.ActivityLoadScreenBinding
import android.widget.Button
import android.widget.Toast
import com.tugas.aplikasimonitoringgudang.R
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.data.session.AppPreferences
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel

class LoadScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadScreenBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkHelper = NetworkHelper(this)

        // cek koneksi internet
        if (networkHelper.isConnected()) {
            loadScreen()
        } else {
            AlertConnect()
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.kuning)

        viewModel.sinkronisasiDataUser()
    }

    private fun loadScreen() {
        Handler().postDelayed({
            AppPreferences.init(this)
            val userId = AppPreferences.getUserId()
            val username = AppPreferences.getUsername()
            val isLoggedIn = AppPreferences.isLoggedIn()

            val intent = if (isLoggedIn) {
                Toast.makeText(this, "Selamat Datang Kembali, $username!", Toast.LENGTH_SHORT).show()
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, AwalActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, 1000)
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
        dialogView.findViewById<Button>(R.id.btn_coba_lagi).setOnClickListener {
            // Reload Activity untuk cek ulang koneksi
            recreate()
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_batal).setOnClickListener {
            // Tutup aplikasi
            finish()
        }

        // Tampilkan AlertDialog
        alertDialog.show()
    }
}
