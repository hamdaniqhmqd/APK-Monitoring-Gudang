package com.tugas.aplikasimonitoringgudang.ui.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditSupplierBinding
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailSupplierBinding
import com.tugas.aplikasimonitoringgudang.ui.barang.BarangFragment
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel

class AddEditSupplierFragment : Fragment() {

    private lateinit var viewModel: SupplierViewModel
    private var supplierId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddEditSupplierBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        supplierId = arguments?.getInt("supplierId")
        if (supplierId != null) {
            viewModel.getSupplierById(supplierId!!).observe(viewLifecycleOwner) { supplier ->
                binding.etNamaSupplier.setText(supplier.nama_supplier)
                binding.etNoHp.setText(supplier.no_hp_supplier.toString())
                binding.etNIK.setText(supplier.nik_supplier.toString())
            }
        }

        binding.btnSubmit.setOnClickListener {
            val nama = binding.etNamaSupplier.text.toString()
            val no_hp = binding.etNoHp.text.toString().toInt()
            val nik = binding.etNIK.text.toString().toInt()

            if (supplierId != null) {
                viewModel.update(
                    Supplier(
                        id_supplier = supplierId!!,
                        nama_supplier = nama,
                        no_hp_supplier = no_hp,
                        nik_supplier = nik
                    )
                )
            } else {
                viewModel.insert(
                    Supplier(
                        nama_supplier = nama,
                        no_hp_supplier = no_hp,
                        nik_supplier = nik
                    )
                )
            }

            toSupplierFragment()
        }

        return binding.root
    }

    private fun toSupplierFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, SupplierFragment())
            .addToBackStack(null)
            .commit()
    }
}