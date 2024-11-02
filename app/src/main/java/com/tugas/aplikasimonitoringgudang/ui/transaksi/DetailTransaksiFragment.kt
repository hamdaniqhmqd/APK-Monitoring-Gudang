package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailTransaksiBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class DetailTransaksiFragment : Fragment() {
    private lateinit var transaksiViewModel: TransaksiViewModel
    private var transaksiId: Int? = null
    private var status: Int? = 0

    private lateinit var supplierViewModel: SupplierViewModel
    private var supplierId: Int = 0
    private var supplierNama: String = ""

    private lateinit var barangViewModel: BarangViewModel
    private var barang_id: Int = 0
    private var user_id: Int = 0
    private var username: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailTransaksiBinding.inflate(inflater, container, false)
        transaksiViewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        supplierViewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        transaksiId = arguments?.getInt("transaksiId")
        transaksiId?.let { it ->
            transaksiViewModel.getTransaksiById(it).observe(viewLifecycleOwner) { transaksi ->
                binding.tvTransaksiName.text = transaksi.barang_nama
                binding.tvTransaksiHarga.text = transaksi.harga_barang.toString()
                binding.tvTransaksiJumlah.text = transaksi.jumlah_barang.toString()
                binding.tvTransaksiTotal.text = transaksi.total_harga_barang.toString()

                binding.tvBarangName.text = transaksi.barang_nama
                binding.tvBarangCategory.text = transaksi.kategori_barang
                binding.tvBarangPrice.text = transaksi.harga_barang.toString()
                binding.tvBarangStock.text = transaksi.stok_barang.toString()
                binding.tvBarangSizes.text = transaksi.ukuran_barang

                supplierId = transaksi.supplier_id
                var supplier_id = transaksi.supplier_id
                supplier_id.let {
                    supplierViewModel.getSupplierById(it).observe(viewLifecycleOwner) { supplier ->
                        binding.tvSupplierName.text = supplier.nama_supplier
                        binding.tvSupplierPhone.text = supplier.no_hp_supplier.toString()
                        binding.tvSupplierNik.text = supplier.nik_supplier.toString()
                    }
                }
                barang_id = transaksi.barang_id
                user_id = transaksi.user_id
                status = transaksi.status
                if (transaksi.status == 1) {
                    binding.wadahStatus.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.merah_keluar))
                    binding.status.text = "Barang Keluar"
                } else if (transaksi.status == 2) {
                    binding.wadahStatus.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.hijau_masuk))
                    binding.status.text = "Barang Masuk"
                } else if (transaksi.status == 3) {
                    binding.wadahStatus.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.putih_smooth))
                    binding.status.text = "Batal Transaksi"
                }
                if (transaksi.status == 3) {
                    binding.btnBatal.isEnabled = false
                    binding.btnBatal.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.putih_smooth))
                }
            }
        }
//        binding.tvSupplierName.text = supplierId.toString()

        username = (requireActivity() as MainActivity).intentUsername().toString()
        binding.btnBatal.setOnClickListener {
            val namaBarang = binding.tvTransaksiName.text.toString()
            val hargaBarang = binding.tvTransaksiHarga.text.toString().toInt()
            val jumlahBarang = binding.tvTransaksiJumlah.text.toString().toInt()
            val totalHarga = binding.tvTransaksiTotal.text.toString().toInt()
            val kategoriBarang = binding.tvBarangCategory.text.toString()
            val stokBarang = binding.tvBarangStock.text.toString().toInt()
            val ukuranBarang = binding.tvBarangSizes.text.toString()

            val supplierNama = binding.tvSupplierName.text.toString()
            transaksiViewModel.update(
                Transaksi(
                    id_transaksi = transaksiId!!,
                    barang_nama = namaBarang,
                    kategori_barang = kategoriBarang,
                    harga_barang = hargaBarang,
                    stok_barang = stokBarang,
                    ukuran_barang = ukuranBarang,
                    jumlah_barang = jumlahBarang,
                    total_harga_barang = totalHarga,
                    user_id = user_id,
                    user_nama = username,
                    supplier_id = supplierId,
                    supplier_nama = supplierNama,
                    status = 3
                )
            )

            if (status!! == 1) {
                val stokKembali = stokBarang + jumlahBarang
                barangViewModel.update(
                    Barang(
                        id_barang = barang_id,
                        nama_barang = namaBarang,
                        kategori_barang = kategoriBarang,
                        harga_barang = hargaBarang,
                        stok_barang = stokKembali,
                        ukuran_barang = ukuranBarang,
                        supplier_id = supplierId,
                        supplier_nama = supplierNama
                    )
                )
            }
            toTransaksiFragment()
        }
        binding.btnHapus.setOnClickListener {
            transaksiViewModel.delete(
                Transaksi(
                    transaksiId!!, 0,"", "", 0,
                    0, "",0, 0, 0,
                    "", 0, "", 0
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