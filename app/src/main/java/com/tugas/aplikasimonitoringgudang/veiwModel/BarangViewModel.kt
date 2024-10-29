package com.tugas.aplikasimonitoringgudang.veiwModel

import android.app.Application
import android.util.Log
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

    fun insert(barang: Barang) = viewModelScope.launch {
        repository.insert(barang)
    }

    fun update(barang: Barang) = viewModelScope.launch {
        repository.update(barang)
    }

    fun delete(barang: Barang) = viewModelScope.launch {
        repository.delete(barang)
    }

    fun getBarangById(id: Int): LiveData<Barang> {
        return repository.getBarangById(id)
    }

    fun getBarangByIdSupplier(idSupplier: Int): LiveData<List<Barang>> {
        return repository.getBarangBySupplerId(idSupplier)
    }

}