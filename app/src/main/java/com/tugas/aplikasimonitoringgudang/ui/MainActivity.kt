package com.tugas.aplikasimonitoringgudang.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.databinding.ActivityMainBinding
import com.tugas.aplikasimonitoringgudang.ui.barang.BarangFragment
import com.tugas.aplikasimonitoringgudang.ui.supplier.SupplierFragment
import com.tugas.aplikasimonitoringgudang.ui.transaksi.TransaksiFragment
import com.tugas.aplikasimonitoringgudang.ui.user.UserFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

    fun toSupplier() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, SupplierFragment())
            .addToBackStack(null)
            .commit()
    }

    fun toBarang() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, BarangFragment())
            .addToBackStack(null)
            .commit()
    }

    fun intentUsername(): String? {
        val username = intent.getStringExtra("username")
        return username
    }

    fun toTransaksi() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, TransaksiFragment())
            .addToBackStack(null)
            .commit()
    }
}