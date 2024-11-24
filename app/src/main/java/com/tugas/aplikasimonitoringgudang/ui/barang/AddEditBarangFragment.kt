package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.session.AppPreferences
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditBarangBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEditBarangFragment : Fragment() {
    private lateinit var transaksiViewModel: TransaksiViewModel
    private lateinit var barangViewModel: BarangViewModel
    private lateinit var viewModel: SupplierViewModel
    private var barangId: Int = 0
    private var supplierId: Int? = 0
    private var supplierNama: String? = ""

    private val formatTanggal = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private val tanggalSaatIni = formatTanggal.format(Date())

    private val formatBulan = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private val bulanSaatIni = formatBulan.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddEditBarangBinding.inflate(inflater, container, false)

        AppPreferences.init(requireContext())
        val user_id = AppPreferences.getUserId()
        val username = AppPreferences.getUsername()
        val isLoggedIn = AppPreferences.isLoggedIn()

        transaksiViewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        supplierId = arguments?.getInt("supplierId")
        supplierNama = arguments?.getString("supplierNama")

        val kategori = binding.kategoriBarang
        val kategori_item = listOf("Dewasa", "Remaja", "Anak-Anak")
        val adapterKategori = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, kategori_item)
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kategori.adapter = adapterKategori

        val ukuran = binding.ukuranBarang
        val ukuran_item = listOf("XXXL", "XXL", "XL", "L", "M", "S")
        val adapterUkuran = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ukuran_item)
        adapterUkuran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ukuran.adapter = adapterUkuran

        binding.btnSubmit.setOnClickListener {
            val nama = binding.namaBarang.text.toString()
            val kategori = binding.kategoriBarang.selectedItem.toString()
            val harga = binding.hargaBarang.text.toString().toInt()
            val stok = binding.stokBarang.text.toString().toInt()
            val ukuran = binding.ukuranBarang.selectedItem.toString()

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
                    user_id = user_id,
                    user_nama = username.toString(),
                    supplier_id = supplierId!!,
                    supplier_nama = supplierNama!!,
                    bulan = bulanSaatIni,
                    tanggal = tanggalSaatIni,
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