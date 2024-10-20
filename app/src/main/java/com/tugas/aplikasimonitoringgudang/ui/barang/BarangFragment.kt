package com.tugas.aplikasimonitoringgudang.ui.barang

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterBarang
import com.tugas.aplikasimonitoringgudang.databinding.FragmentBarangBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel

class BarangFragment : Fragment() {
    private var _binding: FragmentBarangBinding? = null

    private val binding get() = _binding!!

    private lateinit var BarangAdapter: AdapterBarang

    private lateinit var barangViewModel: BarangViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BarangAdapter = AdapterBarang { barang ->
            barang.let {
                barang.id_barang
                barang.nama_barang
                barang.kategori_barang
                barang.stok_barang
                barang.harga_barang
                barang.ukuran_barang
            }
        }

        binding.recyclerViewBarang.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewBarang.adapter = BarangAdapter

        binding.fabAdd.setOnClickListener{
            val intent = Intent(context, BarangInputActivity::class.java)
            startActivity(intent)
        }

    }
}