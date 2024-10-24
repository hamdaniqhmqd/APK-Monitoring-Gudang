package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterTransaksi
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentTransaksiBinding
import com.tugas.aplikasimonitoringgudang.databinding.FragmentUserBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class TransaksiFragment : Fragment() {
    private var _binding: FragmentTransaksiBinding? = null

    private val binding get() = _binding!!

    private lateinit var Adapter: AdapterTransaksi

    private lateinit var viewModel: TransaksiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)

        Adapter = AdapterTransaksi(emptyList()) { transaksi ->
            onDetailClick(transaksi)
        }

        binding.recyclerViewTransaksi.adapter = Adapter
        binding.recyclerViewTransaksi.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allTransaksi.observe(viewLifecycleOwner) { products ->
            products?.let {
                Adapter.setDataTransaksi(it) // Update data di adapter
            }
        }

//        viewModel.getUniqueBarangCountInTransaksiMasuk().observe(this, Observer { count ->
//            binding.transaksiMasukCount.text = count.toString()
//        })

//        binding.fabAddBarang.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.FragmentMenu, AddEditTransaksiFragment())
//                .addToBackStack(null)
//                .commit()
//        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        kode user di bawah ini
//    }

    private fun onDetailClick(transaksi: Transaksi) {
        // Navigasi ke CreateProductFragment dengan ID produk
        val bundle = Bundle().apply {
            putInt("transaksiId", transaksi.id_transaksi ?: 0)
        }
        val detailFragment = DetailTransaksiFragment()
        detailFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}
