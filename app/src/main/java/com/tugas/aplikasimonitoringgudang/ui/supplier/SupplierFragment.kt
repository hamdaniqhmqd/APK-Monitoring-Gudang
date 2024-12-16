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

        // Inisialisasi AdapterSupplier dengan tipe eksplisit untuk parameter lambda
        adapter = AdapterSupplier { supplier: Supplier ->
            onDetailClick(supplier)
        }

        // Menyiapkan RecyclerView
        binding.recyclerViewSupplier.adapter = adapter
        binding.recyclerViewSupplier.layoutManager = GridLayoutManager(requireContext(), 2)

        // Mengamati data dari ViewModel dan mengirimkannya ke adapter
        viewModel.allSupplier.observe(viewLifecycleOwner) { supplierList ->
            supplierList?.let {
                // untuk menampilkan list ke dalam adapter
                adapter.submitList(it)
            }
        }

        // Menyiapkan tombol FAB (Floating Action Button) untuk menambah supplier baru
        binding.fabAdd.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.FragmentMenu, AddEditSupplierFragment())  // Menavigasi ke AddEditSupplierFragment
                .addToBackStack(null)  // Menambahkan transaksi ke back stack agar pengguna bisa kembali
                .commit()  // Menyelesaikan transaksi
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Menetapkan binding ke null saat tampilan dihancurkan untuk menghindari memory leak
    }

    private fun onDetailClick(supplier: Supplier) {
        val bundle = Bundle().apply {
            putLong("supplierId", supplier.id_supplier)  // Mengirimkan ID supplier ke fragment berikutnya
        }
        val detailFragment = DetailSupplierFragment()
        detailFragment.arguments = bundle  // Menetapkan argumen untuk fragment detail

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, detailFragment)  // Mengganti fragment saat ini dengan DetailSupplierFragment
            .addToBackStack(null)  // Menambahkan transaksi ke back stack agar pengguna bisa kembali
            .commit()  // Menyelesaikan transaksi
    }
}
