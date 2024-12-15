package com.tugas.aplikasimonitoringgudang.veiwModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.repository.TransaksiRepository
import kotlinx.coroutines.launch

class TransaksiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransaksiRepository
    val allTransaksi: MutableLiveData<List<Transaksi>> = MutableLiveData() // LiveData untuk daftar transaksi

    init {
        val transaksiDao = GudangDatabase.getDatabase(application).transaksiDao()
        val networkHelper = NetworkHelper(application)
        repository = TransaksiRepository(transaksiDao, networkHelper)
        getAllTransaksi() // Inisialisasi dengan memuat semua data transaksi
    }

    // Fungsi untuk mengambil semua transaksi
    private fun getAllTransaksi() {
        viewModelScope.launch {
            try {
                val transaksiList = repository.getAllTransaksi()
                allTransaksi.postValue(transaksiList)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk mengambil detail transaksi berdasarkan ID
    fun getTransaksiById(id: Long): LiveData<Transaksi> {
        val result = MutableLiveData<Transaksi>()
        viewModelScope.launch {
            try {
                val transaksi = repository.getTransaksiById(id)
                result.postValue(transaksi)
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
        return result
    }

    // Fungsi untuk menambahkan transaksi
    fun insertTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            try {
                repository.addTransaksi(transaksi)
                getAllTransaksi() // Perbarui daftar transaksi setelah penambahan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk memperbarui transaksi
    fun updateTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            try {
                repository.updateTransaksi(transaksi.id_transaksi, transaksi)
                getAllTransaksi() // Perbarui daftar transaksi setelah pembaruan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }

    // Fungsi untuk menghapus transaksi
    fun deleteTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            try {
                repository.deleteTransaksi(transaksi)
                getAllTransaksi() // Perbarui daftar transaksi setelah penghapusan
            } catch (e: Exception) {
                e.printStackTrace() // Logging error
            }
        }
    }
}

