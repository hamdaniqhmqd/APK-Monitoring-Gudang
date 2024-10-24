package com.tugas.aplikasimonitoringgudang.veiwModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.data.transaksi.TransaksiRepository
import kotlinx.coroutines.launch

class TransaksiViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransaksiRepository
    val allTransaksi: LiveData<List<Transaksi>>

    init {
        val transaksiDao = GudangDatabase.getDatabase(application).transakasiDao()
        repository = TransaksiRepository(transaksiDao)
        allTransaksi = repository.allTransaksi
    }

    fun insert(transaksi: Transaksi) = viewModelScope.launch {
        repository.insert(transaksi)
    }

    fun update(transaksi: Transaksi) = viewModelScope.launch {
        repository.update(transaksi)
    }

    fun delete(transaksi: Transaksi) = viewModelScope.launch {
        repository.delete(transaksi)
    }

    fun getTransaksiById(id: Int): LiveData<Transaksi> {
        return repository.getTransaksiById(id)
    }

    fun getUniqueBarangCountInTransaksiMasuk(): LiveData<Int> {
        return repository.getUniqueBarangCountInTransaksiMasuk()
    }
}
