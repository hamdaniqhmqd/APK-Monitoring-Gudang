package com.tugas.aplikasimonitoringgudang.ui.barang

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.databinding.FragmentDetailBarangBinding
import com.tugas.aplikasimonitoringgudang.ui.transaksi.AddEditTransaksiFragment
import com.tugas.aplikasimonitoringgudang.veiwModel.BarangViewModel
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel

class DetailBarangFragment : Fragment() {
    private var _binding: FragmentDetailBarangBinding? = null
    private val binding get() = _binding!!

    private lateinit var barangViewModel: BarangViewModel
    private var barangId: Long? = null

    private lateinit var viewModel: SupplierViewModel
    private var supplierId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBarangBinding.inflate(inflater, container, false)

        //inisialisasi viewmodel
        barangViewModel = ViewModelProvider(this).get(BarangViewModel::class.java)
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        //menga,bil barangId dan supp dari argumen
        barangId = arguments?.getLong("barangId")
        supplierId = arguments?.getLong("supplierId")

        //setup ui elemen dan listener
        setupBarangDetails()
        setupSupplierDetails()
        setupButtonListeners()

        return binding.root
    }


    private fun setupBarangDetails() {
        barangId?.let { id ->
            //mengamati perubahan pda barang dgn id yang ditentukan
            barangViewModel.getBarangById(id).observe(viewLifecycleOwner) { barang ->
                binding.tvItemName.text = barang.nama_barang
                binding.tvItemCategoryIsi.text = barang.kategori_barang
                binding.tvItemPriceIsi.text = barang.harga_barang.toString()
                binding.tvItemStockIsi.text = barang.stok_barang.toString()
                binding.tvItemSizesIsi.text = barang.ukuran_barang
            }
        }
    }

    private fun setupSupplierDetails() {
        supplierId?.let { id ->
            viewModel.getSupplierById(id).observe(viewLifecycleOwner) { supplier ->
                binding.tvSupplierName.text = supplier.nama_supplier
                binding.tvSupplierPhoneIsi.text = supplier.no_hp_supplier.toString()
                binding.tvSupplierNikIsi.text = supplier.nik_supplier.toString()
            }
        }
    }

    private fun setupButtonListeners() {
        binding.btnEdit.setOnClickListener {
            onEditClick(barangId ?: return@setOnClickListener)
        }

        binding.btnTransaksi.setOnClickListener {
            onAddTransaksiClick(barangId ?: return@setOnClickListener, supplierId ?: return@setOnClickListener)
        }

        binding.btnHapus.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    //fungsi menampilkan configurasi dialog
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Hapus")
            .setMessage("APAKAH ANDA YAKIN INGIN MENGHAPUS?")
            //memanggil fungsi if user klik YA
            .setPositiveButton("YA") { _, _ ->
                deleteBarang()
            }
            .setNegativeButton("TIDAK", null)
            .show()
    }

    private fun deleteBarang() {
        barangId?.let { id ->
            barangViewModel.delete(Barang(id, "", "", 0, 0, "", 0, ""))
            toBarangFragment()
        }
    }

    private fun onEditClick(idBarang: Long) {
        val bundle = Bundle().apply {
            putLong("barangId", idBarang)
        }
        val addEditFragment = EditBarangFragment()
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

    private fun onAddTransaksiClick(idBarang: Long, idSupplier: Long) {
        val bundle = Bundle().apply {
            putLong("barangId", idBarang) //melewati barangId dan suppId ke Add Edit Fragmen
            putLong("supplierId", idSupplier)
        }
        val addEditTransaksiFragment = AddEditTransaksiFragment()
        addEditTransaksiFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, addEditTransaksiFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}