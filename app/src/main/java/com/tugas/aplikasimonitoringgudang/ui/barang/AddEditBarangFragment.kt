package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditBarangBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel

class AddEditBarangFragment : Fragment() {
    //    private lateinit var binding: FragmentAddEditBarangBinding
    private lateinit var barangViewModel: BarangViewModel
    private lateinit var viewModel: SupplierViewModel
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
        // Inflate the layout for this fragment
        val binding = FragmentAddEditBarangBinding.inflate(inflater, container, false)

        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        supplierId = arguments?.getInt("supplierId")
        supplierNama = arguments?.getString("supplierNama")

        binding.btnSubmit.setOnClickListener {
            val nama = binding.namaBarang.text.toString()
            val kategori = binding.kategoriBarang.text.toString()
            val harga = binding.hargaBarang.text.toString().toInt()
            val stok = binding.stokBarang.text.toString().toInt()
            val ukuran = binding.ukuranBarang.text.toString()

            barangViewModel.insert(
                Barang(
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