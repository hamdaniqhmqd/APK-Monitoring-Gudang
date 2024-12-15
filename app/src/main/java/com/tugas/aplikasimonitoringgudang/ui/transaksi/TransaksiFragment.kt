package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterTransaksi
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.databinding.FragmentTransaksiBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.UserViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterTransaksi
    private lateinit var transaksiViewModel: TransaksiViewModel
    private lateinit var barangViewModel: BarangViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var supplierViewModel: SupplierViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transaksiViewModel = ViewModelProvider(this).get(TransaksiViewModel::class.java)
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        supplierViewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)

        // Inisialisasi Adapter dengan callback untuk klik detail
        adapter = AdapterTransaksi(
            onItemClick = { transaksi ->
                onDetailClick(transaksi)
            },
            barangViewModel = barangViewModel,
            userViewModel = userViewModel,
            supplierViewModel = supplierViewModel,
            context = requireContext()
        )
        binding.recyclerViewTransaksi.adapter = adapter
        binding.recyclerViewTransaksi.layoutManager = LinearLayoutManager(requireContext())

        // Observasi data transaksi
        transaksiViewModel.allTransaksi.observe(viewLifecycleOwner) { transaksiList ->
            if (transaksiList.isNullOrEmpty()) {
                binding.recyclerViewTransaksi.visibility = View.GONE
                binding.infoDataKosong.visibility = View.VISIBLE
            } else {
                binding.recyclerViewTransaksi.visibility = View.VISIBLE
                binding.infoDataKosong.visibility = View.GONE
                setHeader(transaksiList)
            }
        }

        // Konfigurasi pencarian
        setupSearch()

        // Kembalikan root view
        return binding.root
    }

    private fun setupSearch() {
        val searchEditText =
            binding.inputSearch.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)

        // Atur visibilitas label berdasarkan fokus pada input pencarian
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.label.visibility = if (hasFocus) View.GONE else View.VISIBLE
        }

        // Listener untuk input pencarian
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

    private fun performSearch(searchText: String?) {
        transaksiViewModel.allTransaksi.observe(viewLifecycleOwner) { transaksiList ->
            if (!transaksiList.isNullOrEmpty()) {
                val pencarian = mutableListOf<Transaksi>()
                var totalBarangSelesai = 0
                transaksiList.forEach { transaksi ->
                    barangViewModel.getBarangById(transaksi.barang_id)
                        .observe(viewLifecycleOwner) { barang ->
                            if (barang != null) {
                                totalBarangSelesai++
                                if (barang.nama_barang.contains(
                                        searchText ?: "",
                                        ignoreCase = true
                                    )
                                ) {
                                    pencarian.add(transaksi)
                                }

                                // Perbarui RecyclerView hanya jika semua barang selesai diproses
                                if (totalBarangSelesai == transaksiList.size) {
                                    updateRecyclerView(pencarian)
                                }
                            }
                        }
                }
            } else {
                updateRecyclerView(emptyList())
            }
        }
    }

    private fun updateRecyclerView(pencarian: List<Transaksi>) {
        if (pencarian.isEmpty()) {
            binding.recyclerViewTransaksi.visibility = View.GONE
            binding.infoDataKosong.visibility = View.VISIBLE
        } else {
            binding.recyclerViewTransaksi.visibility = View.VISIBLE
            binding.infoDataKosong.visibility = View.GONE
            setHeader(pencarian)
        }
    }


    private fun setHeader(transaksiList: List<Transaksi>) {
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

        adapter.submitList(data)
    }

    private fun onDetailClick(transaksi: Transaksi) {
        val bundle = Bundle().apply {
            putLong("transaksiId", transaksi.id_transaksi ?: 0)
        }
        val detailFragment = DetailTransaksiFragment()
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
