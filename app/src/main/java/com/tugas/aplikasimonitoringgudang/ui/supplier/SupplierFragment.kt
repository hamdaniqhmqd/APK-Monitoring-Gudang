package com.tugas.aplikasimonitoringgudang.ui.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterSupplier
import com.tugas.aplikasimonitoringgudang.databinding.FragmentSupplierBinding
import com.tugas.aplikasimonitoringgudang.databinding.FragmentUserBinding

class SupplierFragment : Fragment() {
    private var _binding: FragmentSupplierBinding? = null

    private val binding get() = _binding!!

    private lateinit var SupplierAdapter: AdapterSupplier

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSupplierBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        kode user di bawah ini
    }
}