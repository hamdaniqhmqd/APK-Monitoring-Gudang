package com.tugas.aplikasimonitoringgudang.ui.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterSupplier
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.databinding.FragmentSupplierBinding
import com.tugas.aplikasimonitoringgudang.ui.supplier.AddEditSupplierFragment
import com.tugas.aplikasimonitoringgudang.ui.supplier.DetailSupplierFragment
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel

class SupplierFragment : Fragment() {
    private var _binding: FragmentSupplierBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AdapterSupplier
    private lateinit var viewModel: SupplierViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSupplierBinding.inflate(inflater, container, false)

        // Initialize AdapterSupplier with explicit type for the lambda parameter
        adapter = AdapterSupplier { supplier: Supplier ->
            onDetailClick(supplier)
        }

        // Set up RecyclerView
        binding.recyclerViewSupplier.adapter = adapter
        binding.recyclerViewSupplier.layoutManager = GridLayoutManager(requireContext(), 2)

        // Observe data from ViewModel and submit to adapter
        viewModel.allSupplier.observe(viewLifecycleOwner) { supplierList ->
            supplierList?.let {
                // untuk menampilkan list adapter
                adapter.submitList(it)
            }
        }

        binding.fabAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.FragmentMenu, AddEditSupplierFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onDetailClick(supplier: Supplier) {
        val bundle = Bundle().apply {
            putLong("supplierId", supplier.id_supplier)
        }
        val detailFragment = DetailSupplierFragment()
        detailFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}
