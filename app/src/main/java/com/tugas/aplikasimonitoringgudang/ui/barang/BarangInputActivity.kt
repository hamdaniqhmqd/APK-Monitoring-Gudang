package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.barang.BarangDao
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.databinding.ActivityInputBarangBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel

class BarangInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBarangBinding
    private val barangViewModel: BarangViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInputBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = GudangDatabase.getDatabase(this)
        var barangDao = database.barangDao()

//        binding.btnSubmit.setOnClickListener {
//            val nama = binding.namaBarang.text.toString()
//            val kategori = binding.kategoriBarang.text.toString()
//            val harga = binding.hargaBarang.text.toString().toInt()
//            val stok = binding.stokBarang.text.toString().toInt()
//            val ukuran = binding.ukuranBarang.text.toString()
//
//               barangViewModel.insert(Barang(
//                nama_barang = nama,
//                kategori_barang = kategori,
//                harga_barang = harga,
//                stok_barang = stok,
//                ukuran_barang = ukuran
//            ))
//        }
    }
}