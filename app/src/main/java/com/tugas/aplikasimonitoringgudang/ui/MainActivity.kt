package com.tugas.aplikasimonitoringgudang.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.databinding.ActivityMainBinding
import com.tugas.aplikasimonitoringgudang.ui.barang.BarangFragment
import com.tugas.aplikasimonitoringgudang.ui.supplier.SupplierFragment
import com.tugas.aplikasimonitoringgudang.ui.transaksi.TransaksiFragment
import com.tugas.aplikasimonitoringgudang.ui.user.UserFragment
import com.tugas.aplikasimonitoringgudang.ui.user.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    fun intentUsername(): String {
        val user_name = intent.getStringExtra("username").toString()
        return user_name
    }
    fun intentUserid(): Int {
        val user_id = intent.getIntExtra("user_id", 0)
        return user_id
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