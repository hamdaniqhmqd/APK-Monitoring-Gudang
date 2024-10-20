package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditTransaksiBinding
import com.tugas.aplikasimonitoringgudang.databinding.FragmentTransaksiBinding
import com.tugas.aplikasimonitoringgudang.ui.MainActivity
import com.tugas.aplikasimonitoringgudang.ui.barang.AddEditBarangFragment
import com.tugas.aplikasimonitoringgudang.ui.barang.BarangFragment
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class AddEditTransaksiFragment : Fragment() {

    private var _binding: FragmentAddEditTransaksiBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: TransaksiViewModel
    private lateinit var barangViewModel: BarangViewModel

    private var transaksiId: Int? = null
    private var barangId: Int? = null

    private var jumlahBarang: Int = 0
    private var totalHargaBarang: Int? = 0
    private var hargaBarang: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditTransaksiBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)

        barangId = arguments?.getInt("barangId")
        if (barangId != null) {
            barangViewModel.getBarangById(barangId!!).observe(viewLifecycleOwner) { barang ->
//                if (barang != null) {
                binding.namaBarang.text = barang.nama_barang
                binding.hargaBarang.text = barang.harga_barang.toString()
//                }
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

//        transaksiId = arguments?.getInt("transaksiId")
//        if (transaksiId != null) {
//            viewModel.getTransaksiById(transaksiId!!).observe(viewLifecycleOwner) { transaksi ->
//                if (transaksi != null) {
//                    binding.namaBarang.setText(transaksi.barang_nama)
//                    binding.hargaBarang.setText(transaksi.harga_barang.toString())
//                }
//            }
//        }

        binding.saveBtn.setOnClickListener {
            val namaBarang = binding.namaBarang.text.toString()
            val hargaBarang = binding.hargaBarang.text.toString().toInt()
            val jumlahBarang = binding.jumlahBarang.text.toString().toInt()
            val totalHarga = binding.totalHargaBarang.text.toString().toInt()

            viewModel.insert(
                Transaksi(
                    barang_nama = namaBarang,
                    harga_barang = hargaBarang,
                    jumlah_barang = jumlahBarang,
                    total_harga_barang = totalHarga,
                    status = 1
                )
            )

            toTransaksiFragment()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun toTransaksiFragment() {
        (requireActivity() as MainActivity).toTransaksi()
    }
}