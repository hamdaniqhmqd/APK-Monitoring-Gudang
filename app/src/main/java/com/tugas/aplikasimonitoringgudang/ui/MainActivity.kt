package com.tugas.aplikasimonitoringgudang.ui

import android.os.Bundle
import android.view.View
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
    private lateinit var userViewModel: UserViewModel
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