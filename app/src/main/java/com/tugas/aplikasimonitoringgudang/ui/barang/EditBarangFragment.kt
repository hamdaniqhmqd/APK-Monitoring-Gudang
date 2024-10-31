package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.FragmentEditBarangBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel

class EditBarangFragment : Fragment() {
    private lateinit var barangViewModel: BarangViewModel
    private var barangId: Int? = -1
    private var supplierId: Int? = -1
    private var supplierNama: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditBarangBinding.inflate(inflater, container, false)

        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)

        barangId = arguments?.getInt("barangId")
        if (barangId != -1) {
            barangViewModel.getBarangById(barangId!!).observe(viewLifecycleOwner) { barang ->
                if (barang != null) {
                    binding.namaBarang.setText(barang.nama_barang)
                    binding.kategoriBarang.setText(barang.kategori_barang)
                    binding.hargaBarang.setText(barang.harga_barang.toString())
                    binding.stokBarang.setText(barang.stok_barang.toString())
                    binding.ukuranBarang.setText(barang.ukuran_barang)

                    supplierId = barang.supplier_id
                    supplierNama = barang.supplier_nama
                }
            }
        }

        supplierId = arguments?.getInt("supplierId")

        binding.btnSubmit.setOnClickListener {
            val nama = binding.namaBarang.text.toString()
            val kategori = binding.kategoriBarang.text.toString()
            val harga = binding.hargaBarang.text.toString().toInt()
            val stok = binding.stokBarang.text.toString().toInt()
            val ukuran = binding.ukuranBarang.text.toString()

        barangViewModel.update(
                    Barang(
                        id_barang = barangId!!,
                        nama_barang = nama,
                        kategori_barang = kategori,
                        harga_barang = harga,
                        stok_barang = stok,
                        ukuran_barang = ukuran,
                        supplier_id = supplierId!!,
                        supplier_nama = supplierNama!!
                    )
                )

            toBarangFragment()
        }

        return binding.root
    }

    private fun toBarangFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, BarangFragment())
            .addToBackStack(null)
            .commit()
    }
}