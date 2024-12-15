package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.session.AppPreferences
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditTransaksiBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEditTransaksiFragment : Fragment() {
    private lateinit var viewModel: TransaksiViewModel
    private lateinit var barangViewModel: BarangViewModel
    private lateinit var supplierViewModel: SupplierViewModel

    private var barangId: Long? = null
    private var kategoriBarang: String? = ""
    private var stokBarang: Int? = 0
    private var ukuranBarang: String? = ""
    private var jumlahBarang: Int = 0
    private var totalHargaBarang: Int? = 0
    private var hargaBarang: Int? = 0

    private var supplierId: Long? = null

    private val formatTanggal = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private val tanggalSaatIni = formatTanggal.format(Date())

    private val formatBulan = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private val bulanSaatIni = formatBulan.format(Date())

    private var cekKoneksi: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).navigasiHilang()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddEditTransaksiBinding.inflate(inflater, container, false)

        val networkHelper = NetworkHelper(requireContext())

        // Inisialisasi ViewModels
        AppPreferences.init(requireContext())
        val user_id = AppPreferences.getUserId()
        val username = AppPreferences.getUsername()
        val isLoggedIn = AppPreferences.isLoggedIn()

        viewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        supplierViewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        // Ambil ID Barang dari argument
        barangId = arguments?.getLong("barangId") ?: 0
        if (barangId != null) {
            barangViewModel.getBarangById(barangId!!).observe(viewLifecycleOwner) { barang ->
                binding.namaBarang.text = barang.nama_barang
                kategoriBarang = barang.kategori_barang
                stokBarang = barang.stok_barang
                ukuranBarang = barang.ukuran_barang
                binding.hargaBarang.text = barang.harga_barang.toString()
                supplierId = barang.supplier_id
            }
        }

        // Tombol untuk menambah jumlah barang
        binding.plus.setOnClickListener {
            if (stokBarang!! > jumlahBarang) {
                jumlahBarang += 1
            }
            binding.jumlahBarang.text = jumlahBarang.toString()
            hargaBarang = binding.hargaBarang.text.toString().toInt()
            totalHargaBarang = hargaBarang!! * jumlahBarang
            binding.totalHargaBarang.text = totalHargaBarang.toString()
        }

        // Tombol untuk mengurangi jumlah barang
        binding.minus.setOnClickListener {
            if (jumlahBarang > 0) {
                jumlahBarang -= 1
            }
            binding.jumlahBarang.text = jumlahBarang.toString()
            hargaBarang = binding.hargaBarang.text.toString().toInt()
            totalHargaBarang = hargaBarang!! * jumlahBarang
            binding.totalHargaBarang.text = totalHargaBarang.toString()
        }

        // Tombol untuk menyimpan transaksi
        binding.saveBtn.setOnClickListener {
            val namaBarang = binding.namaBarang.text.toString()
            val hargaBarang = binding.hargaBarang.text.toString().toInt()
            val jumlahBarang = binding.jumlahBarang.text.toString().toInt()
            val totalHarga = binding.totalHargaBarang.text.toString().toInt()
            val sisa_stok = stokBarang!! - jumlahBarang
            val id_transaksi: Long = System.currentTimeMillis()

            val transaksiUpdated = Transaksi(
                id_transaksi = id_transaksi,
                barang_id = barangId!!,
                jumlah_barang = jumlahBarang,
                total_harga_barang = totalHarga,
                user_id = user_id,
                supplier_id = supplierId!!,
                bulan = bulanSaatIni,
                tanggal = tanggalSaatIni,
                tanggalAkhir = tanggalSaatIni,
                status = 1,
                statusAkhir = 0,
            )

            // Update stok barang setelah transaksi
            val updateBarang = Barang(
                id_barang = barangId!!,
                nama_barang = namaBarang,
                kategori_barang = kategoriBarang!!,
                harga_barang = hargaBarang,
                stok_barang = sisa_stok,
                ukuran_barang = ukuranBarang!!,
                supplier_id = supplierId!!,
            )

            if (networkHelper.isConnected()) {
                cekKoneksi = true
            } else {
                AlertConnect()
            }

            if (cekKoneksi == true) {
                // Menyimpan transaksi baru dan mengubah data transaksi
                viewModel.insertTransaksi(transaksiUpdated)
                barangViewModel.update(updateBarang)

                // Kembali ke fragment transaksi setelah menyimpan data
                toTransaksiFragment()
            }
        }

        return binding.root
    }

    private fun AlertConnect() {
        // Inflate custom layout
        val dialogView = layoutInflater.inflate(R.layout.notifikasi_alert_dialog, null)

        // Bangun AlertDialog
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView) // Gunakan layout kustom
            .setCancelable(false) // Tidak bisa ditutup kecuali klik tombol
            .create()

        // Atur aksi untuk tombol
        dialogView.findViewById<Button>(R.id.btn_muat_ulang).setOnClickListener {
            // Reload Activity untuk cek ulang koneksi
            requireActivity().recreate()
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_ya).setOnClickListener {
            // lanjut ke act atau fragmnet yang dipilih
            cekKoneksi = true
        }

        // Tampilkan AlertDialog
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).navigasiMuncul()
    }

    private fun toTransaksiFragment() {
        (requireActivity() as MainActivity).toTransaksi()
        (requireActivity() as MainActivity).navigasiMuncul()
    }
}
