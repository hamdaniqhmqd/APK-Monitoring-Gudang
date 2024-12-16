package com.tugas.aplikasimonitoringgudang.ui.supplier

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.adapter.AdapterBarang
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailSupplierBinding
import com.tugas.aplikasimonitoringgudang.ui.barang.AddEditBarangFragment
import com.tugas.aplikasimonitoringgudang.ui.barang.DetailBarangFragment
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel

class DetailSupplierFragment : Fragment() {

    // Deklarasi properti ViewModel dan variabel
    private lateinit var viewModel: SupplierViewModel
    private var supplierId: Long? = null
    private var supplierNama: String? = ""
    private lateinit var Adapter: AdapterBarang
    private lateinit var viewModelBarang: BarangViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi binding
        val binding = FragmentDetailSupplierBinding.inflate(inflater, container, false)

        // Inisialisasi ViewModel Supplier
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        // Mendapatkan supplierId dari arguments
        supplierId = arguments?.getLong("supplierId")

        // Menampilkan detail supplier jika supplierId tersedia
        supplierId?.let {
            viewModel.getSupplierById(it).observe(viewLifecycleOwner) { supplier ->
                supplierNama = supplier.nama_supplier
                binding.nama.text = supplier.nama_supplier
                binding.noHp.text = supplier.no_hp_supplier.toString()
                binding.nik.text = supplier.nik_supplier.toString()
            }
        }

        // Tombol Edit
        binding.btnEdit.setOnClickListener {
            onEditClick(supplierId!!)
        }

        // Tombol Hapus
        binding.btnHapus.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi Hapus")
            builder.setMessage("Apakah Anda yakin ingin menghapus supplier ini?")
            builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
            builder.setPositiveButton("Hapus") { dialog, _ ->
                supplierId?.let {
                    viewModel.delete(Supplier(it, "", "", ""))
                    toSupplierFragment()
                }
                dialog.dismiss()
            }
            builder.create().show()
        }

        // Tombol Tambah Barang
        binding.btnTambahBarang.setOnClickListener {
            onTambahBarangClick(supplierId!!, supplierNama!!)
        }

        // Inisialisasi ViewModel dan Adapter Barang
        viewModelBarang = ViewModelProvider(this).get(BarangViewModel::class.java)
        Adapter = AdapterBarang() { barang -> onDetailClick(barang) }

        binding.rvBarang.adapter = Adapter
        binding.rvBarang.layoutManager = LinearLayoutManager(requireContext())

        // Menampilkan daftar barang
        supplierId?.let {
            viewModelBarang.getAllBarangByIdSupplier(it).observe(viewLifecycleOwner) { barangList ->
                barangList?.let { setHeader(it) }
            }
        }

        return binding.root
    }

    // Fungsi untuk menampilkan header berdasarkan kategori barang
    fun setHeader(barangList: List<Barang>) {
        val data: MutableList<Any> = mutableListOf()
        data.clear()
        val sortedMap = barangList.sortedBy { it.kategori_barang }

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

    // Navigasi ke fragment edit supplier
    private fun onEditClick(idSupplier: Long) {
        val bundle = Bundle().apply { putLong("supplierId", idSupplier) }
        val addEditFragment = AddEditSupplierFragment()
        addEditFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, addEditFragment)
            .addToBackStack(null)
            .commit()
    }

    // Navigasi ke daftar supplier
    private fun toSupplierFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, SupplierFragment())
            .addToBackStack(null)
            .commit()
    }

    // Navigasi ke detail barang
    private fun onDetailClick(barang: Barang) {
        val bundle = Bundle().apply {
            putLong("barangId", barang.id_barang ?: 0)
            putLong("supplierId", barang.supplier_id)
        }
        val detailFragment = DetailBarangFragment()
        detailFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    // Navigasi ke fragment tambah barang
    private fun onTambahBarangClick(idSupplier: Long, namaSupplier: String) {
        val bundle = Bundle().apply {
            putLong("supplierId", idSupplier)
            putString("supplierNama", namaSupplier)
        }
        val addEditFragment = AddEditBarangFragment()
        addEditFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, addEditFragment)
            .addToBackStack(null)
            .commit()
    }
}
