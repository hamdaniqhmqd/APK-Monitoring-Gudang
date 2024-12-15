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
        MutableLiveData() // LiveData untuk daftar barang
    val allBarangByIdSupplier: MutableLiveData<List<Barang>> = MutableLiveData()

    init {
        val barangDao = GudangDatabase.getDatabase(application).barangDao()
        val networkHelper = NetworkHelper(application)
        repository = BarangRepository(barangDao, networkHelper)
        getAllBarang() // Inisialisasi dengan memuat semua data barang
    }

    // Fungsi untuk mengambil semua barang
    private fun getAllBarang() {
        viewModelScope.launch {
            try {
                val barangList = repository.getAllBarang()
                allBarang.postValue(barangList)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk mengambil detail barang berdasarkan ID
    fun getBarangById(id: Long): LiveData<Barang> {
        val result = MutableLiveData<Barang>()
        viewModelScope.launch {
            try {
                val barang = repository.getBarangById(id)
                result.postValue(barang)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
        return result
    }

    // Fungsi untuk menambahkan barang dan transaksi
    fun insert(
        barang: Barang
    ) {
        viewModelScope.launch {
            try {
                // Insert barang dan dapatkan idBarang
                val idBarang = repository.insert(barang)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fungsi untuk memperbarui barang
    fun update(barang: Barang) {
        viewModelScope.launch {
            try {
                repository.update(barang.id_barang, barang)
                getAllBarang() // Perbarui daftar barang setelah pembaruan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk menghapus barang
    fun delete(barang: Barang) {
        viewModelScope.launch {
            try {
                repository.delete(barang)
                getAllBarang() // Perbarui daftar barang setelah penghapusan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk mengambil semua barang
    fun getAllBarangByIdSupplier(id: Long): LiveData<List<Barang>> {
        val result = MutableLiveData<List<Barang>>()
        viewModelScope.launch {
            try {
                val barangList = repository.getBarangBySupplierId(id)
                result.postValue(barangList)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
        return result
    }
}
