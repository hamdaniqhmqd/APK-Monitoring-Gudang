package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditBarangBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class AddEditBarangFragment : Fragment() {
    private lateinit var transaksiViewModel: TransaksiViewModel
    private lateinit var barangViewModel: BarangViewModel
    private lateinit var viewModel: SupplierViewModel
    private var barangId: Int? = -1
    private var supplierId: Int? = -1
    private var supplierNama: String? = ""
    private var user_id: Int? = 0
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        val binding = FragmentAddEditBarangBinding.inflate(inflater, container, false)

        transaksiViewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        supplierId = arguments?.getInt("supplierId")
        supplierNama = arguments?.getString("supplierNama")

        user_id = (requireActivity() as MainActivity).intentUserid()
        username = (requireActivity() as MainActivity).intentUsername()

        binding.btnSubmit.setOnClickListener {
            val nama = binding.namaBarang.text.toString()
            val kategori = binding.kategoriBarang.text.toString()
            val harga = binding.hargaBarang.text.toString().toInt()
            val stok = binding.stokBarang.text.toString().toInt()
            val ukuran = binding.ukuranBarang.text.toString()

            val totalHarga = harga * stok

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

            transaksiViewModel.insert(
                Transaksi(
//                    barang_id = barangId!!,
                    barang_nama = nama,
                    kategori_barang = kategori,
                    harga_barang = harga,
                    stok_barang = stok,
                    ukuran_barang = ukuran,
                    jumlah_barang = stok,
                    total_harga_barang = totalHarga,
                    user_id = user_id!!,
                    user_nama = username,
                    supplier_id = supplierId!!,
                    supplier_nama = supplierNama!!,
                    status = 2
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