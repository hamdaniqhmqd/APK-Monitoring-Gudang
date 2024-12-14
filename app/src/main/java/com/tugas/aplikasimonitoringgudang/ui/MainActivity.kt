package com.tugas.aplikasimonitoringgudang.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.databinding.ActivityMainBinding
import com.tugas.aplikasimonitoringgudang.ui.barang.BarangFragment
import com.tugas.aplikasimonitoringgudang.ui.supplier.SupplierFragment
import com.tugas.aplikasimonitoringgudang.ui.transaksi.TransaksiFragment
import com.tugas.aplikasimonitoringgudang.ui.admin.UserFragment
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var doubleBackToExitPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.kuning)

        binding.Navigasi.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.HomeNavigasi -> selectedFragment = UserFragment()
                R.id.BarangNavigasi -> selectedFragment = BarangFragment()
                R.id.SupplierNavigasi -> selectedFragment = SupplierFragment()
                R.id.TransaksiNavigasi -> selectedFragment = TransaksiFragment()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.FragmentMenu, selectedFragment)
                    .commit()
            }
            true
        }
        if (savedInstanceState == null) {
            binding.Navigasi.selectedItemId = R.id.HomeNavigasi
        }
    }

    override fun onBackPressed() {
        // close aplikasi jika tombol back ditekan dua kali
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan lagi untuk keluar", Toast.LENGTH_SHORT).show()

        // set delay selama 2 detik agar pengguna tidak menekan back dua kali secara tidak sengaja
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    fun toTransaksi() {
        binding.Navigasi.selectedItemId = R.id.TransaksiNavigasi
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, TransaksiFragment())
            .addToBackStack(null)
            .commit()
    }

    fun navigasiHilang() {
        binding.Navigasi.visibility = View.GONE
    }

    fun navigasiMuncul() {
        binding.Navigasi.visibility = View.VISIBLE
    }
}