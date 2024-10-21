package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailTransaksiBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class DetailTransaksiFragment : Fragment() {
    private lateinit var viewModel: TransaksiViewModel
    private var transaksiId: Int? = null
    private var status: Int? = null

    private var supplierNama: String? = ""
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailTransaksiBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)

        transaksiId = arguments?.getInt("transaksiId")
        transaksiId?.let {
            viewModel.getTransaksiById(it).observe(viewLifecycleOwner) { transaksi ->
                status = transaksi.status
                supplierNama = transaksi.supplier_nama
                binding.tvTransaksiName.text = transaksi.barang_nama
                binding.tvTransaksiHarga.text = transaksi.harga_barang.toString()
                binding.tvTransaksiJumlah.text = transaksi.jumlah_barang.toString()
                binding.tvTransaksiTotal.text = transaksi.total_harga_barang.toString()
            }
        }

        if (status == 1) {
            binding.wadahStatus.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), // Menggunakan requireContext untuk fragment
                    com.tugas.aplikasimonitoringgudang.R.color.merah_keluar
                )
            )
            binding.status.text = "Barang Keluar"
        } else if (status == 2) {
            binding.wadahStatus.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.tugas.aplikasimonitoringgudang.R.color.hijau_masuk
                )
            )
            binding.status.text = "Barang Masuk"
        } else if (status == 3) {
            binding.wadahStatus.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.tugas.aplikasimonitoringgudang.R.color.putih_smooth
                )
            )
            binding.status.text = "Batal Transaksi"
        }

        username = (requireActivity() as MainActivity).intentUsername().toString()

        binding.btnBatal.setOnClickListener {
            val namaBarang = binding.tvTransaksiName.text.toString()
            val hargaBarang = binding.tvTransaksiHarga.text.toString().toInt()
            val jumlahBarang = binding.tvTransaksiJumlah.text.toString().toInt()
            val totalHarga = binding.tvTransaksiTotal.text.toString().toInt()

            viewModel.update(
                Transaksi(
                    id_transaksi = transaksiId!!,
                    barang_nama = namaBarang,
                    user_nama = username,
                    supplier_nama = supplierNama!!,
                    harga_barang = hargaBarang,
                    jumlah_barang = jumlahBarang,
                    total_harga_barang = totalHarga,
                    status = 3
                )
            )

            toTransaksiFragment()
        }

        binding.btnHapus.setOnClickListener {
            viewModel.delete(Transaksi(transaksiId!!, "", "", "", 0, 0, 0, 0))
            toTransaksiFragment()
        }

        return binding.root
    }

    private fun toTransaksiFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, TransaksiFragment())
            .addToBackStack(null)
            .commit()
    }
}