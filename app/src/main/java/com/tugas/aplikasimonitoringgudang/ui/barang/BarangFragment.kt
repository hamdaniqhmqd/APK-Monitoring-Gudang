package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterBarang
import com.tugas.aplikasimonitoringgudang.adapter.AdapterUser
import com.tugas.aplikasimonitoringgudang.databinding.FragmentBarangBinding
import com.tugas.aplikasimonitoringgudang.databinding.FragmentUserBinding

class BarangFragment : Fragment() {
    private var _binding: FragmentBarangBinding? = null

    private val binding get() = _binding!!

    private lateinit var BarangAdapter: AdapterBarang

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarangBinding.inflate(inflater, container, false)
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