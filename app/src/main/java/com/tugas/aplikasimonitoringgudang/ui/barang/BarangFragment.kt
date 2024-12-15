package com.tugas.aplikasimonitoringgudang.ui.barang

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
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

        Adapter = AdapterBarang() { barang ->
            onDetailClick(barang)
        }

        binding.recyclerViewBarang.adapter = Adapter

        binding.recyclerViewBarang.apply {
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (adapter?.getItemViewType(position)) {
                            AdapterBarang.ItemType.BARANG.ordinal -> 1
                            AdapterBarang.ItemType.HEADER.ordinal -> 2
                            else -> 1
                        }
                    }
                }
            }
        }

        viewModel.allBarang.observe(viewLifecycleOwner) { barang ->
            barang?.let {
                setHeader(it)
            }
        }

        return binding.root
    }

    fun setHeader(barangList: List<Barang>) {
        val data: MutableList<Any> = mutableListOf()
        data.clear()

        val sortedMap = barangList.sortedBy { barang ->
            barang.kategori_barang
        }

        if (sortedMap.isNotEmpty()) {
            var dataKategori = sortedMap[0].kategori_barang
            data.add(dataKategori)
            for (element in sortedMap) {
                if (element.kategori_barang != dataKategori) {
                    dataKategori = element.kategori_barang
                    data.add(dataKategori)
                }
                data.add(element)
            }
        }

        Adapter.submitList(data)
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
            putLong("barangId", barang.id_barang ?: 0)
            putLong("supplierId", barang.supplier_id ?: 0)
        }
        val detailFragment = DetailBarangFragment()
        detailFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}