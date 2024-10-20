package com.tugas.aplikasimonitoringgudang.veiwModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.barang.BarangRepository
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import kotlinx.coroutines.launch

class BarangViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BarangRepository
    val allBarang: LiveData<List<Barang>>

    init {
        val barangDao = GudangDatabase.getDatabase(application).barangDao()
        repository = BarangRepository(barangDao)
        allBarang = repository.allBarang
    }

    fun insert(transaksi: Barang) = viewModelScope.launch {
        repository.insert(transaksi)
    }

    fun update(transaksi: Barang) = viewModelScope.launch {
        repository.update(transaksi)
    }

    fun delete(transaksi: Barang) = viewModelScope.launch {
        repository.delete(transaksi)
    }

    suspend fun getBarangById(id: Int): Barang? {
        return repository.getBarangById(id)
    }
}