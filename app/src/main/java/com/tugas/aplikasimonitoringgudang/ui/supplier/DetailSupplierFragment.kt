package com.tugas.aplikasimonitoringgudang.ui.supplier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterBarang
import com.tugas.aplikasimonitoringgudang.adapter.AdapterSupplier
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailSupplierBinding
import com.tugas.aplikasimonitoringgudang.ui.barang.AddEditBarangFragment
import com.tugas.aplikasimonitoringgudang.ui.barang.DetailBarangFragment
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel

class DetailSupplierFragment : Fragment() {

    private lateinit var viewModel: SupplierViewModel
    private var supplierId: Int? = null
    private var supplierNama: String? = ""

    private lateinit var Adapter: AdapterBarang
    private lateinit var viewModelBarang: BarangViewModel


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
                supplierNama = supplier.nama_supplier
                binding.nama.text = supplier.nama_supplier
                binding.noHp.text = supplier.no_hp_supplier.toString()
                binding.nik.text = supplier.nik_supplier.toString()
            }
        }

        binding.btnEdit.setOnClickListener {
            onEditClick(supplierId!!)
        }

        binding.btnHapus.setOnClickListener {
            viewModel.delete(Supplier(supplierId!!, "", "", ""))
            toSupplierFragment()
        }

        binding.btnTambahBarang.setOnClickListener {
            onTambahBarangClick(supplierId!!, supplierNama!!)
        }

        viewModelBarang = ViewModelProvider(this).get(BarangViewModel::class.java)

        Adapter = AdapterBarang() { barang ->
            onDetailClick(barang)
        }

        binding.rvBarang.adapter = Adapter
        binding.rvBarang.layoutManager = LinearLayoutManager(requireContext())

        supplierId?.let {
            viewModelBarang.getAllBarangByIdSupplier(it).observe(viewLifecycleOwner) { barangList ->
                barangList?.let {
                    // Use 'it' safely here
                    setHeader(it)
                }
            }
        }


        return binding.root
    }

    fun setHeader(barangList: List<Barang>) {
        val data: MutableList<Any> = mutableListOf()
        data.clear()

        val sortedMap = barangList.sortedBy {
            it.kategori_barang
        }

        if (sortedMap.isNotEmpty()) {
            var dataKategori = sortedMap[0].kategori_barang
            data.add(dataKategori)
            for (element in sortedMap) {
                if (element.kategori_barang != dataKategori) {
                    dataKategori = element.kategori_barang
                    data.add(dataKategori)
                }
                data.add(element)
            }
        }

        Adapter.submitList(data)
    }
    private fun onEditClick(idSupplier: Int) {
        // Navigasi ke CreateProductFragment dengan ID produk
        val bundle = Bundle().apply {
            putInt("supplierId", idSupplier ?: 0)
        }
        val addEditFragment = AddEditSupplierFragment()
        addEditFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, addEditFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun toSupplierFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, SupplierFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun onDetailClick(barang: Barang) {
        // Navigasi ke CreateProductFragment dengan ID produk
        val bundle = Bundle().apply {
            putInt("barangId", barang.id_barang ?: 0)
            putInt("supplierId", barang.supplier_id ?: 0)
        }
        val detailFragment = DetailBarangFragment()
        detailFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onTambahBarangClick(idSupplier: Int, namaSupplier: String) {
        val bundle = Bundle().apply {
            putInt("supplierId", idSupplier ?: 0)
            putString("supplierNama", namaSupplier ?: "")
        }
        val addEditFragment = AddEditBarangFragment()
        addEditFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, addEditFragment)
            .addToBackStack(null)
            .commit()
    }
}