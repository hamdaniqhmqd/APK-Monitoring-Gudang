package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.BarangDao
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.databinding.ActivityBarangBinding

class BarangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarangBinding
    private lateinit var barangDao: BarangDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = GudangDatabase.getDatabase(this)
        barangDao = database.barangDao()


    }


}