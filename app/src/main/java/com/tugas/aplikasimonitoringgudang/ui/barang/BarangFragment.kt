package com.tugas.aplikasimonitoringgudang.ui.barang

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterBarang
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.FragmentBarangBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel

class BarangFragment : Fragment() {
    private var _binding: FragmentBarangBinding? = null

    private val binding get() = _binding!!

    private lateinit var Adapter: AdapterBarang

    private lateinit var viewModel: BarangViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarangBinding.inflate(inflater, container, false)

        Adapter = AdapterBarang(emptyList()) { barang ->
            onDetailClick(barang)
        }

        binding.recyclerViewBarang.adapter = Adapter
        binding.recyclerViewBarang.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allBarang.observe(viewLifecycleOwner) { barang ->
            barang?.let {
                Adapter.setBarangList(it)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

    private fun onDetailClick(barang: Barang) {
        // Navigasi ke CreateProductFragment dengan ID produk
        val bundle = Bundle().apply {
            putInt("barangId", barang.id_barang ?: 0)
            putInt("supplierId", barang.supplier_id ?: 0)
        }
        val detailFragment = DetailBarangFragment()
        detailFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}