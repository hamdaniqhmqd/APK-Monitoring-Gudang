package com.tugas.aplikasimonitoringgudang.ui.transaksi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterTransaksi
import com.tugas.aplikasimonitoringgudang.adapter.AdapterUser
import com.tugas.aplikasimonitoringgudang.databinding.FragmentTransaksiBinding
import com.tugas.aplikasimonitoringgudang.databinding.FragmentUserBinding

class TransaksiFragment : Fragment() {
    private var _binding: FragmentTransaksiBinding? = null

    private val binding get() = _binding!!

    private lateinit var TransaksiAdapter: AdapterTransaksi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
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