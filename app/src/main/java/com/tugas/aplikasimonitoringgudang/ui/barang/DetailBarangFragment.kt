package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditBarangBinding
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailBarangBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel

class DetailBarangFragment : Fragment() {
    private lateinit var barangViewModel: BarangViewModel
    private var barangId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDetailBarangBinding.inflate(inflater, container, false)

        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)

        barangId = arguments?.getInt("barangId")
        barangId?.let {
            barangViewModel.getBarangById(it).observe(viewLifecycleOwner) { barang ->
                binding.tvItemName.text = barang.nama_barang
                binding.tvItemCategoryIsi.text = barang.kategori_barang
                binding.tvItemPriceIsi.text = barang.harga_barang.toString()
                binding.tvItemStockIsi.text = barang.stok_barang.toString()
                binding.tvItemSizesIsi.text = barang.ukuran_barang
            }
        }

        binding.btnEdit.setOnClickListener {
            barangId?.let { id ->
                onDetailClick(id)
            }
        }

        binding.btnHapus.setOnClickListener {
            barangId?.let { id ->
                barangViewModel.delete(Barang(id, "", "", 0, 0, ""))
            }

            toBarangFragment()
        }

        return binding.root
    }

    private fun onDetailClick(idBarang: Int) {
        // Navigasi ke CreateProductFragment dengan ID produk
        val bundle = Bundle().apply {
            putInt("barangId", idBarang ?: 0)
        }
        val addEditFragment = AddEditBarangFragment()
        addEditFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, addEditFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun toBarangFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, BarangFragment())
            .addToBackStack(null)
            .commit()
    }
}