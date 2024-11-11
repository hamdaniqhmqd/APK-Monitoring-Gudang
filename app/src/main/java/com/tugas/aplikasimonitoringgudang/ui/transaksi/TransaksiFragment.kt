package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            products?.let {
                setHeader(it)
            }
        }
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