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
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEditBarangFragment : Fragment() {
    //inisialisasi viewmodel
    private lateinit var transaksiViewModel: TransaksiViewModel
    private lateinit var barangViewModel: BarangViewModel
    private lateinit var viewModel: SupplierViewModel

    private var supplierId: Long? = 0

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

        //digunakan u/ mengelola data sesi pengguna like user_id, unamer dan status login (u/ verif atau melacak siapa yang membuat/edit)
        AppPreferences.init(requireContext())
        val user_id = AppPreferences.getUserId()
        val username = AppPreferences.getUsername()
        val isLoggedIn = AppPreferences.isLoggedIn()

        //inisialisasi viewmodel
        transaksiViewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        //mengambil data dari Argumen, pada fragment ini mengambil suplierId yang dikirim dari fragment sebelumnya. digunakan u/ menghubungkan barang dengan suplier yang sesuai
        supplierId = arguments?.getLong("supplierId")

        val kategori = binding.kategoriBarang

        //ini membuat dropdown dengan menggunakan spinner
        val kategori_item = listOf("Dewasa", "Remaja", "Anak-Anak")
        val adapterKategori =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, kategori_item)
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kategori.adapter = adapterKategori

        //membuat object barang dari data form
        val ukuran = binding.ukuranBarang
        val ukuran_item = listOf("XXXL", "XXL", "XL", "L", "M", "S")
        val adapterUkuran =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ukuran_item)
        adapterUkuran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ukuran.adapter = adapterUkuran


        //objek transaksi unntuk mencatat perubahan stok barang
        binding.btnSubmit.setOnClickListener {
            val nama = binding.namaBarang.text.toString()
            val kategori = binding.kategoriBarang.selectedItem.toString()
            val harga = binding.hargaBarang.text.toString().toInt()
            val stok = binding.stokBarang.text.toString().toInt()
            val ukuran = binding.ukuranBarang.selectedItem.toString()
            val totalHarga = harga * stok
            val id_barang: Long = System.currentTimeMillis()
            val id_transaksi: Long = System.currentTimeMillis()


            //membuat objek berdasarkan data yang di inputkan lalu disimpan ke database dengan menggunakan view model
            val barang = Barang(
                id_barang = id_barang,
                nama_barang = nama,
                kategori_barang = kategori,
                harga_barang = harga,
                stok_barang = stok,
                ukuran_barang = ukuran,
                supplier_id = supplierId!!,
            )
            barangViewModel.insert(barang)

            //membuat objek transaksi u/ mencatat perubahan stok barang
            val transaksi = Transaksi(
                id_transaksi = id_transaksi, // ID transaksi auto-generate
                barang_id = id_barang, // Placeholder, akan diperbarui
                jumlah_barang = stok,
                total_harga_barang = totalHarga,
                user_id = user_id,
                supplier_id = supplierId!!,
                bulan = bulanSaatIni,
                tanggal = tanggalSaatIni,
                tanggalAkhir = tanggalSaatIni,
                status = 2,
                created_at = "",
                updated_at = ""
            )
            transaksiViewModel.insertTransaksi(transaksi)

            //setelah disimpan maka akan berpindah ke BarangFragment
            toBarangFragment()
        }

        //taampilkan layout
        return binding.root
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }
    //berpindah ke BarangFragment
    private fun toBarangFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, BarangFragment())
            .addToBackStack(null)
            .commit()
    }
}