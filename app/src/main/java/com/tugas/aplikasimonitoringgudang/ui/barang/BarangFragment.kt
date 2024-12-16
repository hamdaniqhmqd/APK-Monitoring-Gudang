package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterBarang
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.FragmentBarangBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel


class BarangFragment : Fragment() {
    private var _binding: FragmentBarangBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterBarang
    private lateinit var viewModel: BarangViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
    }

    //layout dan adapter recycler view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarangBinding.inflate(inflater, container, false)

        adapter = AdapterBarang { barang ->
            onDetailClick(barang)
        }

        binding.recyclerViewBarang.adapter = adapter
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

        viewModel.allBarang.observe(viewLifecycleOwner) { barangList ->
            if (barangList.isNullOrEmpty()) {
                binding.recyclerViewBarang.visibility = View.GONE
                binding.infoDataKosong.visibility = View.VISIBLE
            } else {
                binding.recyclerViewBarang.visibility = View.VISIBLE
                binding.infoDataKosong.visibility = View.GONE
                setHeader(barangList)
            }
        }

        setupSearch()

        return binding.root
    }

    //mengatur searchview agar dapat menerima input dari pencarian
    private fun setupSearch() {
        val searchEditText = binding.inputSearch.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.label.visibility = if (hasFocus) View.GONE else View.VISIBLE
        }

        binding.inputSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                performSearch(searchText)
                return true
            }
        })
    }

    //menampilkan barang yang sesuai dengan kata kunci search
    private fun performSearch(searchText: String?) {
        viewModel.allBarang.value?.let { barangList ->
            val filteredList = if (searchText.isNullOrBlank()) {
                barangList
            } else {
                barangList.filter { barang ->
                    barang.kategori_barang.contains(searchText, ignoreCase = true) ||
                            barang.nama_barang.contains(searchText, ignoreCase = true)
                }
            }
            updateRecyclerView(filteredList)
        }
    }

    //menyembunyikan RecyclerView jiika data kosong/update jika data ada
    private fun updateRecyclerView(barangList: List<Barang>) {
        if (barangList.isEmpty()) {
            binding.recyclerViewBarang.visibility = View.GONE
            binding.infoDataKosong.visibility = View.VISIBLE
        } else {
            binding.recyclerViewBarang.visibility = View.VISIBLE
            binding.infoDataKosong.visibility = View.GONE
            setHeader(barangList)
        }
    }

    //header kategori diatas barang sesuai kategori
    private fun setHeader(barangList: List<Barang>) {
        val data: MutableList<Any> = mutableListOf()
        val sortedMap = barangList.sortedBy { it.kategori_barang }

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

        adapter.submitList(data)
    }

    //
    private fun onDetailClick(barang: Barang) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}