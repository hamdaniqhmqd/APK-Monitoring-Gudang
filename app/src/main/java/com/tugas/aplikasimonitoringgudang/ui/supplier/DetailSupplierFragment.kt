package com.tugas.aplikasimonitoringgudang.ui.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterSupplier
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailSupplierBinding
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailTransaksiBinding
import com.tugas.aplikasimonitoringgudang.ui.transaksi.AddEditTransaksiFragment
import com.tugas.aplikasimonitoringgudang.ui.transaksi.TransaksiFragment
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.TransaksiViewModel

class DetailSupplierFragment : Fragment() {

    private lateinit var viewModel: SupplierViewModel
    private var supplierId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDetailSupplierBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        supplierId = arguments?.getInt("supplierId")
        supplierId?.let {
            viewModel.getSupplierById(it).observe(viewLifecycleOwner) { supplier ->
                binding.nama.text = supplier.nama_supplier
                binding.noHp.text = supplier.no_hp_supplier.toString()
                binding.nik.text = supplier.nik_supplier.toString()
            }
        }

        binding.btnEdit.setOnClickListener {
            supplierId?.let { id ->
                onEditClick(id)
            }
        }

        binding.btnHapus.setOnClickListener {
            supplierId?.let { id ->
                viewModel.delete(Supplier(id, "", 0, 0))
            }
            toTransaksiFragment()
        }

        return binding.root
    }

    private fun onEditClick(idTransaksi: Int) {
        // Navigasi ke CreateProductFragment dengan ID produk
        val bundle = Bundle().apply {
            putInt("transaksiId", idTransaksi ?: 0)
        }
        val addEditFragment = AddEditSupplierFragment()
        addEditFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, addEditFragment)
            .addToBackStack(null)
            .commit()
    }
    private fun toTransaksiFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, SupplierFragment())
            .addToBackStack(null)
            .commit()
    }
}