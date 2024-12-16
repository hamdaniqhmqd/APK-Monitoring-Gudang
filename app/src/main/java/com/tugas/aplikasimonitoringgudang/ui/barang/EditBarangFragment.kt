package com.tugas.aplikasimonitoringgudang.ui.barang

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.FragmentEditBarangBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel

class EditBarangFragment : Fragment() {

    private lateinit var barangViewModel: BarangViewModel

    //ID barang dan suplier didapat dari argumen fragment
    private var barangId: Long? = 0
    private var supplierId: Long? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //menghubungkan fragmen ke
        val binding = FragmentEditBarangBinding.inflate(inflater, container, false)

        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)

        barangId = arguments?.getLong("barangId")
        if (barangId != 0.toLong()) {
            barangViewModel.getBarangById(barangId!!).observe(viewLifecycleOwner) { barang ->
                if (barang != null) {
                    binding.namaBarang.setText(barang.nama_barang)
                    binding.hargaBarang.setText(barang.harga_barang.toString())
                    binding.stokBarang.setText(barang.stok_barang.toString())

                    val kategori_item = listOf("Dewasa", "Remaja", "Anak-Anak")
                    val ukuran_item = listOf("XXXL", "XXL", "XL", "L", "M", "S")
                    binding.kategoriBarang.post {
                        val kategoriIndex = kategori_item.indexOf(barang.kategori_barang)
                        if (kategoriIndex != -1) {
                            binding.kategoriBarang.setSelection(kategoriIndex)
                        }
                    }

                    binding.ukuranBarang.post {
                        val ukuranIndex = ukuran_item.indexOf(barang.ukuran_barang)
                        if (ukuranIndex != -1) {
                            binding.ukuranBarang.setSelection(ukuranIndex)
                        }
                    }

                    supplierId = barang.supplier_id
                }
            }
        }

        supplierId = arguments?.getLong("supplierId")


        val kategori = binding.kategoriBarang
        val kategori_item = listOf("Dewasa", "Remaja", "Anak-Anak")
        val adapterKategori =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, kategori_item)
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kategori.adapter = adapterKategori

        val ukuran = binding.ukuranBarang
        val ukuran_item = listOf("XXXL", "XXL", "XL", "L", "M", "S")
        val adapterUkuran =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ukuran_item)
        adapterUkuran.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ukuran.adapter = adapterUkuran

        binding.btnSubmit.setOnClickListener {
            val nama = binding.namaBarang.text.toString()
            val kategori = binding.kategoriBarang.selectedItem.toString()
            val harga = binding.hargaBarang.text.toString().toInt()
            val stok = binding.stokBarang.text.toString().toInt()
            val ukuran = binding.ukuranBarang.selectedItem.toString()

            barangViewModel.update(
                Barang(
                    id_barang = barangId!!,
                    nama_barang = nama,
                    kategori_barang = kategori,
                    harga_barang = harga,
                    stok_barang = stok,
                    ukuran_barang = ukuran,
                    supplier_id = supplierId!!,
                )
            )

            toBarangFragment()
        }

        return binding.root
    }

    private fun toBarangFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, BarangFragment())
            .addToBackStack(null)
            .commit()
    }
}