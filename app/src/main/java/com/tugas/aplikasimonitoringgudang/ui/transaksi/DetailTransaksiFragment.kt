package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailTransaksiBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.ui.user.UserViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailTransaksiFragment : Fragment() {
    private lateinit var transaksiViewModel: TransaksiViewModel
    private var transaksiId: Int? = null
    private var status: Int? = 0

    private lateinit var supplierViewModel: SupplierViewModel
    private var supplierId: Int = 0
    private var supplierNama: String = ""

    private lateinit var barangViewModel: BarangViewModel
    private var barangId: Int = 0

    private lateinit var userViewModel: UserViewModel
    private var userId: Int = 0
    private var username: String = ""

    private var tanggalSaatIni = ""
    private var bulanSaatIni = ""

    private val formatTanggal = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private val tanggalSekarang = formatTanggal.format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).navigasiHilang()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailTransaksiBinding.inflate(inflater, container, false)
        transaksiViewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        supplierViewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        transaksiId = arguments?.getInt("transaksiId")
        transaksiId?.let { id ->
            transaksiViewModel.getTransaksiById(id).observe(viewLifecycleOwner) { transaksi ->
                // Set data Barang sesuai id barang di ttransaksi
                barangId = transaksi.barang_id
                barangViewModel.getBarangById(transaksi.barang_id).observe(viewLifecycleOwner) { barang ->
                    binding.tvTransaksiName.text = barang.nama_barang
                    binding.tvTransaksiHarga.text = barang.harga_barang.toString()
                    binding.tvBarangName.text = barang.nama_barang
                    binding.tvBarangCategory.text = barang.kategori_barang
                    binding.tvBarangPrice.text = barang.harga_barang.toString()
                    binding.tvBarangStock.text = barang.stok_barang.toString()
                    binding.tvBarangSizes.text = barang.ukuran_barang
                }


                binding.tvTransaksiJumlah.text = transaksi.jumlah_barang.toString()
                binding.tvTransaksiTotal.text = transaksi.total_harga_barang.toString()

                val formatTanggalDetail = SimpleDateFormat("HH:mm EEEE, d MMMM yyyy", Locale("id", "ID"))
                val awal = formatTanggal.parse(transaksi.tanggal)
                val tanggalAwal = formatTanggalDetail.format(awal!!)
                binding.tanggalTransaksi.text = tanggalAwal

                bulanSaatIni = transaksi.bulan
                tanggalSaatIni = transaksi.tanggal

                supplierId = transaksi.supplier_id
                supplierViewModel.getSupplierById(supplierId).observe(viewLifecycleOwner) { supplier ->
                    binding.tvSupplierName.text = supplier.nama_supplier
                    binding.tvSupplierPhone.text = supplier.no_hp_supplier
                    binding.tvSupplierNik.text = supplier.nik_supplier
                }

                userId = transaksi.user_id
                userViewModel.getUserById(transaksi.user_id).observe(viewLifecycleOwner) { user ->
                    username = user.username
                }

                status = transaksi.status

                if (status == 1) {
                    binding.wadahStatus.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.merah_keluar))
                    binding.status.text = "Barang Keluar"
                } else if (status == 2) {
                    binding.wadahStatus.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.hijau_masuk))
                    binding.status.text = "Barang Masuk"
                    binding.btnBatal.isEnabled = false
                    binding.btnBatal.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.putih_smooth))
                }

                if (transaksi.statusAkhir == 3) {
                    binding.wadahStatusAkhir.visibility = View.VISIBLE
                    binding.wadahStatusAkhir.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.putih_smooth))
                    binding.statusAkhir.text = "Batal Transaksi"
                    val akhir = formatTanggal.parse(transaksi.tanggalAkhir)
                    val tanggalAkhir = formatTanggalDetail.format(akhir!!)
                    binding.tanggalTransaksiAkhir.text = tanggalAkhir
                    binding.btnBatal.isEnabled = false
                    binding.btnBatal.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.putih_smooth))
                }

                binding.btnBatal.setOnClickListener {
                    val namaBarang = binding.tvTransaksiName.text.toString()
                    val hargaBarang = binding.tvTransaksiHarga.text.toString().toInt()
                    val jumlahBarang = binding.tvTransaksiJumlah.text.toString().toInt()
                    val totalHarga = binding.tvTransaksiTotal.text.toString().toInt()
                    val kategoriBarang = binding.tvBarangCategory.text.toString()
                    val stokBarang = binding.tvBarangStock.text.toString().toInt()
                    val ukuranBarang = binding.tvBarangSizes.text.toString()
                    val supplierNama = binding.tvSupplierName.text.toString()

                    val transaksiUpdated = Transaksi(
                        id_transaksi = transaksiId!!,
                        barang_id = barangId,
                        jumlah_barang = jumlahBarang,
                        total_harga_barang = totalHarga,
                        user_id = userId,
                        supplier_id = supplierId,
                        bulan = bulanSaatIni,
                        tanggal = tanggalSaatIni,
                        tanggalAkhir = tanggalSekarang,
                        status = transaksi.status,
                        statusAkhir = 3,
                        created_at = "",
                        updated_at = ""
                    )

                    transaksiViewModel.updateTransaksi(transaksiUpdated)

                    if (status == 1) {
                        val stokKembali = stokBarang + jumlahBarang
                        val barangUpdated = Barang(
                            id_barang = barangId,
                            nama_barang = namaBarang,
                            kategori_barang = kategoriBarang,
                            harga_barang = hargaBarang,
                            stok_barang = stokKembali,
                            ukuran_barang = ukuranBarang,
                            supplier_id = supplierId,
                            supplier_nama = supplierNama
                        )

                        barangViewModel.update(barangUpdated)
                    }

                    toTransaksiFragment()
                }
            }
        }

        binding.btnHapus.setOnClickListener {
            transaksiViewModel.deleteTransaksi(transaksiId!!)
            toTransaksiFragment()
        }

        return binding.root
    }

    private fun toTransaksiFragment() {
        (requireActivity() as MainActivity).toTransaksi()
        (requireActivity() as MainActivity).navigasiMuncul()
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).navigasiMuncul()
    }
}