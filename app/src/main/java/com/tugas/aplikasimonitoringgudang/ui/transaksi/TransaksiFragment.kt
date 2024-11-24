package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.view.marginTop
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        Adapter = AdapterTransaksi { transaksi ->
            onDetailClick(transaksi)
        }
        binding.recyclerViewTransaksi.adapter = Adapter
        binding.recyclerViewTransaksi.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allTransaksi.observe(viewLifecycleOwner) { products ->
            if (products.isNullOrEmpty()) {
                binding.recyclerViewTransaksi.visibility = View.GONE
                binding.infoDataKosong.visibility = View.VISIBLE
            } else {
                binding.recyclerViewTransaksi.visibility = View.VISIBLE
                binding.infoDataKosong.visibility = View.GONE
                setHeader(products)
            }
        }

        val searchEditText = binding.inputSearch.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.label.visibility = View.GONE
            } else {
                binding.label.visibility = View.VISIBLE
            }
        }

        binding.inputSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                val pencarian = viewModel.allTransaksi.value?.filter {
                    it.barang_nama.contains(searchText ?: "", ignoreCase = true)
                } ?: emptyList()

                if (pencarian.isEmpty()) {
                    binding.recyclerViewTransaksi.visibility = View.GONE
                    binding.infoDataKosong.visibility = View.VISIBLE
                } else {
                    binding.recyclerViewTransaksi.visibility = View.VISIBLE
                    binding.infoDataKosong.visibility = View.GONE
                    setHeader(pencarian)
                }
                return true
            }
        })

        return binding.root
    }

    fun setHeader(transaksiList: List<Transaksi>) {
        val data: MutableList<Any> = mutableListOf()
        data.clear()

        val sortedMap = transaksiList.sortedBy { transaksi ->
            transaksi.bulan
        }

        if (sortedMap.isNotEmpty()) {
            var dataBulan = sortedMap[0].bulan
            data.add(dataBulan)
            for (element in sortedMap) {
                if (element.bulan != dataBulan) {
                    dataBulan = element.bulan
                    data.add(dataBulan)
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

    private fun onDetailClick(transaksi: Transaksi) {
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