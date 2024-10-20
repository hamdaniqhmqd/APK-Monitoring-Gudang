package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailTransaksiBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class DetailTransaksiFragment : Fragment() {
    private lateinit var viewModel: TransaksiViewModel
    private var transaksiId: Int? = null

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
//            viewModel.getTransaksiById(it).observe(viewLifecycleOwner) { transaksi ->
//                binding.tvNama.text = transaksi.nama
//                binding.tvHarga.text = transaksi.harga.toString()
//                binding.tvDeskripsi.text = transaksi.deskripsi
//            }
        }

//        binding.btnEdit.setOnClickListener {
//            transaksiId?.let { id ->
//                onDetailClick(id)
//            }
//        }

//        binding.btnDelete.setOnClickListener {
//            transaksiId?.let { id ->
//                viewModel.delete(Transaksi(id, "", 0.0, ""))
//            }

            toTransaksiFragment()
//        }

        return binding.root
    }

    private fun onDetailClick(idTransaksi: Int) {
        // Navigasi ke CreateProductFragment dengan ID produk
        val bundle = Bundle().apply {
            putInt("transaksiId", idTransaksi ?: 0)
        }
        val addEditFragment = AddEditTransaksiFragment()
        addEditFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, addEditFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun toTransaksiFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, TransaksiFragment())
            .addToBackStack(null)
            .commit()
    }
}