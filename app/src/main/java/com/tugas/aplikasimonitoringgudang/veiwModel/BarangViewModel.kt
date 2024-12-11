package com.tugas.aplikasimonitoringgudang.veiwModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.repository.BarangRepository
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import kotlinx.coroutines.launch

class BarangViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BarangRepository
    val allBarang: MutableLiveData<List<Barang>> =
        MutableLiveData() // LiveData untuk daftar supplier
    val allBarangByIdSupplier: MutableLiveData<List<Barang>> = MutableLiveData()

    init {
        val barangDao = GudangDatabase.getDatabase(application).barangDao()
        val networkHelper = NetworkHelper(application)
        repository = BarangRepository(barangDao, networkHelper)
        getAllBarang() // Inisialisasi dengan memuat semua data supplier
    }

    // Fungsi untuk mengambil semua supplier
    private fun getAllBarang() {
        viewModelScope.launch {
            try {
                val supplierList = repository.getAllBarang()
                allBarang.postValue(supplierList)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk mengambil detail supplier berdasarkan ID
    fun getBarangById(id: Int): LiveData<Barang> {
        val result = MutableLiveData<Barang>()
        viewModelScope.launch {
            try {
                val supplier = repository.getBarangById(id)
                result.postValue(supplier)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
        return result
    }

    // Fungsi untuk menambahkan supplier
    fun insert(
        barang: Barang, transaksi: Transaksi, transaksiViewModel: TransaksiViewModel
    ) {
        viewModelScope.launch {
            try {
                // Insert barang dan dapatkan idBarang
                val idBarang = repository.insert(barang)

                // Update transaksi dengan idBarang
                val updatedTransaksi = transaksi.copy(
                    barang_id = idBarang.id_barang
                )

                // Insert transaksi dengan idBarang yang diperbarui
                transaksiViewModel.insertTransaksi(updatedTransaksi)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fungsi untuk memperbarui supplier
    fun update(supplier: Barang) {
        viewModelScope.launch {
            try {
                repository.update(supplier.supplier_id, supplier)
                getAllBarang() // Perbarui daftar supplier setelah pembaruan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk menghapus supplier
    fun delete(supplier: Barang) {
        viewModelScope.launch {
            try {
                repository.delete(supplier)
                getAllBarang() // Perbarui daftar supplier setelah penghapusan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk mengambil semua supplier
    fun getAllBarangByIdSupplier(id: Int): LiveData<List<Barang>> {
        val result = MutableLiveData<List<Barang>>()
        viewModelScope.launch {
            try {
                val supplierList = repository.getBarangBySupplierId(id)
                result.postValue(supplierList)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
        return result
    }
}
