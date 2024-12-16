// Package tempat kode ini berada
package com.tugas.aplikasimonitoringgudang.ui.supplier

// Import pustaka yang diperlukan
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tugas.aplikasimonitoringgudang.R
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.databinding.FragmentAddEditSupplierBinding
import com.tugas.aplikasimonitoringgudang.veiwModel.SupplierViewModel

// Definisi kelas Fragment untuk menambah dan mengedit data supplier
class AddEditSupplierFragment : Fragment() {

    // ViewModel yg diambil dri supplier view model.variabel
    private lateinit var viewModel: SupplierViewModel
    private var supplierId: Long? = null


    // Fungsi ini terdapat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan layout XML dengan kode menggunakan View Binding
        val binding = FragmentAddEditSupplierBinding.inflate(inflater, container, false)
        //  ViewModel untuk mengelola data Supplier
        viewModel = ViewModelProvider(this).get(SupplierViewModel::class.java)

        // berfungsi untuk mengambil nilai ID supplier yang dikirimkan dari fragment sebelumnya + menyimpannya dalam variabel supplierId.
        supplierId = arguments?.getLong("supplierId")

        // logika if, dimana supplierid tidak boleh null
        if (supplierId != null) {
            // dan karena isinya supplierid maka akan menjalankan fungsi getsupplierbyid, tdk blh null
            viewModel.getSupplierById(supplierId!!).observe(viewLifecycleOwner) { supplier ->
                // dan menjalankan proses mengisi form dengan data yang diambil dari database
                binding.etNamaSupplier.setText(supplier.nama_supplier)
                // elemen etnamasupplier diambil dari data class supplier pada variabel nama_supplier, dan seterusnya
                binding.etNoHp.setText(supplier.no_hp_supplier.toString())
                binding.etNIK.setText(supplier.nik_supplier.toString())
            }
        }

        // Tombol Submit untuk menyimpan data saat pengguna
        binding.btnSubmit.setOnClickListener {
            //  elemen btnsubmit, ketika diklik, pada variabel nama  Mengambil data  form elemen etnamasupplier, dan seterusnya
            val nama = binding.etNamaSupplier.text.toString()
            val no_hp = binding.etNoHp.text.toString()
            val nik = binding.etNIK.text.toString()
            val id_supplier: Long = System.currentTimeMillis()

            //  supplierid tidak boleh null Memeriksa apakah ini proses edit atau tambah data baru
            if (supplierId != null) {
                // Jika sedang mengedit data, perbarui data di database
                // maka pada data di data class supplier akan mengambil nama id supllier
                // diambil dari supplierId yang telah diterima dari fragment sebelumnya.
                viewModel.update(
                    Supplier(
                        id_supplier = supplierId!!, //diupdate dengan supplierid yang sudah ada
                        nama_supplier = nama,
                        no_hp_supplier = no_hp,
                        nik_supplier = nik
                    )
                )
            } else {
                // dan Jika menambah data baru, maka data class pada supplier tetap sesuai yang ada dan di masukkan data ke database
                viewModel.insert(
                    Supplier(
                        id_supplier = id_supplier, //disini membuat id unik baru dan ditambahkan ke database
                        nama_supplier = nama,
                        no_hp_supplier = no_hp,
                        nik_supplier = nik
                    )
                )
            }

            // Setelah selesai, kembali ke daftar Supplier
            toSupplierFragment()
        }

        return binding.root
    }

    // Fungsi untuk menavigasi kembali ke daftar Supplier
    private fun toSupplierFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.FragmentMenu, SupplierFragment())  // Mengganti fragment dengan SupplierFragment
            .addToBackStack(null)  //  Menyimpannya dalam BackStack,
            //agar user dapat kembali ke halaman sebelumnya
            .commit()
    }
}
