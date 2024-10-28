package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditTransaksiBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class AddEditTransaksiFragment : Fragment() {
    private lateinit var viewModel: TransaksiViewModel
    private lateinit var barangViewModel: BarangViewModel
    private lateinit var supplierViewModel: SupplierViewModel
    private var barangId: Int? = null
    private var supplierNama: String? = ""
    private var jumlahBarang: Int = 0
    private var totalHargaBarang: Int? = 0
    private var hargaBarang: Int? = 0
    private var username: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddEditTransaksiBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        supplierViewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
        barangId = arguments?.getInt("barangId")
        if (barangId != null) {
            barangViewModel.getBarangById(barangId!!).observe(viewLifecycleOwner) { barang ->
                binding.namaBarang.text = barang.nama_barang
                binding.hargaBarang.text = barang.harga_barang.toString()
                supplierNama = barang.supplier_nama
            }
        }
        binding.plus.setOnClickListener {
            jumlahBarang += 1
            binding.jumlahBarang.text = jumlahBarang.toString()
            hargaBarang = binding.hargaBarang.text.toString().toInt()
            totalHargaBarang = hargaBarang!! * jumlahBarang
            binding.totalHargaBarang.text = totalHargaBarang.toString()
        }
        binding.minus.setOnClickListener {
            if (jumlahBarang > 0) {
                jumlahBarang -= 1
            }
            binding.jumlahBarang.text = jumlahBarang.toString()
            hargaBarang = binding.hargaBarang.text.toString().toInt()
            totalHargaBarang = hargaBarang!! * jumlahBarang
            binding.totalHargaBarang.text = totalHargaBarang.toString()
        }
        username = (requireActivity() as MainActivity).intentUsername().toString()
        binding.saveBtn.setOnClickListener {
            val namaBarang = binding.namaBarang.text.toString()
            val hargaBarang = binding.hargaBarang.text.toString().toInt()
            val jumlahBarang = binding.jumlahBarang.text.toString().toInt()
            val totalHarga = binding.totalHargaBarang.text.toString().toInt()
            viewModel.insert(
                Transaksi(
                    barang_nama = namaBarang,
                    harga_barang = hargaBarang,
                    user_nama = username,
                    supplier_nama = supplierNama!!,
                    jumlah_barang = jumlahBarang,
                    total_harga_barang = totalHarga,
                    status = 2
                )
            )
            toTransaksiFragment()
        }
        return binding.root
    }
    private fun toTransaksiFragment() {
        (requireActivity() as MainActivity).toTransaksi()
    }
}
