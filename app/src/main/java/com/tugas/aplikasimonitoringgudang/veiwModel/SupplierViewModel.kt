package com.tugas.aplikasimonitoringgudang.veiwModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.repository.SupplierRepository
import kotlinx.coroutines.launch

class SupplierViewModel(application: Application) : AndroidViewModel(application) {
// mengakses data dari repository db dan api
    private val repository: SupplierRepository
    val allSupplier: MutableLiveData<List<Supplier>> = MutableLiveData() // LiveData untuk daftar supplier

    init {
        val supplierDao = GudangDatabase.getDatabase(application).supplierDao()
        val networkHelper = NetworkHelper(application)
        repository = SupplierRepository(supplierDao, networkHelper)
        getAllSupplier() // Inisialisasi dengan memuat semua data supplier
    }

    // Fungsi untuk mengambil semua supplier
    private fun getAllSupplier() {
        viewModelScope.launch {
            try {
                val supplierList = repository.getAllSupplier()
                allSupplier.postValue(supplierList)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk mengambil detail supplier berdasarkan ID -> liv dta
    fun getSupplierById(id: Long): LiveData<Supplier> {
        val result = MutableLiveData<Supplier>()
        viewModelScope.launch {
            try {
                val supplier = repository.getSupplierById(id)
                result.postValue(supplier)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
        return result
    }

    // Fungsi untuk menambahkan supplier
    fun insert(supplier: Supplier) {
        viewModelScope.launch {
            try {
                repository.insert(supplier)
                getAllSupplier() // Perbarui daftar supplier setelah penambahan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk memperbarui supplier
    fun update(supplier: Supplier) {
        viewModelScope.launch {
            try {
                repository.update(supplier.id_supplier,supplier)
                getAllSupplier() // Perbarui daftar supplier setelah pembaruan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk menghapus supplier
    fun delete(supplier: Supplier) {
        viewModelScope.launch {
            try {
                repository.delete(supplier)
                getAllSupplier() // Perbarui daftar supplier setelah penghapusan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }
}
