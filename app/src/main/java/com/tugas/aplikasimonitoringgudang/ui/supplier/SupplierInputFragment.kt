package com.tugas.aplikasimonitoringgudang.ui.supplier

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterBarang
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.databinding.FragmentSupplierInputBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel

class SupplierInputFragment : Fragment() {
    private lateinit var binding: FragmentSupplierInputBinding
    private lateinit var supplierViewModel: SupplierViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupplierInputBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_supplier_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            val nama = binding.etNamaSupplier.text.toString()
            val nik = binding.etNIK.text.toString().toInt()
            val no_hp = binding.etNoHp.text.toString().toInt()

            supplierViewModel.insert(
                Supplier(
                    nama_supplier = nama,
                    nik_supplier = nik,
                    no_hp_supplier = no_hp
                )
            )
        }
    }
}