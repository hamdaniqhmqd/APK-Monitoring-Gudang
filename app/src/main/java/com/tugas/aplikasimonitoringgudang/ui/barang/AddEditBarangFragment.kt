package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditBarangBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel

class AddEditBarangFragment : Fragment() {
    //    private lateinit var binding: FragmentAddEditBarangBinding
    private lateinit var barangViewModel: BarangViewModel
    private var barangId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentAddEditBarangBinding.inflate(inflater, container, false)

        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)

        barangId = arguments?.getInt("barangId")
        barangId?.let {
            barangViewModel.getBarangById(it).observe(viewLifecycleOwner) { barang ->
                binding.namaBarang.setText(barang.nama_barang)
                binding.kategoriBarang.setText(barang.kategori_barang)
                binding.hargaBarang.setText(barang.harga_barang)
                binding.stokBarang.setText(barang.stok_barang)
                binding.ukuranBarang.setText(barang.ukuran_barang)
            }
        }

        binding.btnSubmit.setOnClickListener {
            val nama = binding.namaBarang.text.toString()
            val kategori = binding.kategoriBarang.text.toString()
            val harga = binding.hargaBarang.text.toString().toInt()
            val stok = binding.stokBarang.text.toString().toInt()
            val ukuran = binding.ukuranBarang.text.toString()

            if (barangId != null) {
                barangViewModel.update(
                    Barang(
                        id_barang = barangId!!,
                        nama_barang = nama,
                        kategori_barang = kategori,
                        harga_barang = harga,
                        stok_barang = stok,
                        ukuran_barang = ukuran
                    )
                )
            } else {
                barangViewModel.insert(
                    Barang(
                        nama_barang = nama,
                        kategori_barang = kategori,
                        harga_barang = harga,
                        stok_barang = stok,
                        ukuran_barang = ukuran
                    )
                )
            }

            toBarangFragment()
        }

        return binding.root
    }



//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

    private fun toBarangFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, BarangFragment())
            .addToBackStack(null)
            .commit()
    }
}