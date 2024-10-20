package com.tugas.aplikasimonitoringgudang.veiwModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.data.supplier.SupplierDao
import com.tugas.aplikasimonitoringgudang.data.supplier.SupplierRepository
import kotlinx.coroutines.launch

class SupplierViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SupplierRepository
    val allSupplier: LiveData<List<Supplier>>

    init {
        val supplierDao = GudangDatabase.getDatabase(application).supplierDao()
        repository = SupplierRepository(supplierDao)
        allSupplier = repository.allSupplier
    }

    fun insert(supplier: Supplier) = viewModelScope.launch {
        repository.insert(supplier)
    }

    fun update(supplier: Supplier) = viewModelScope.launch {
        repository.update(supplier)
    }

    fun delete(supplier: Supplier) = viewModelScope.launch {
        repository.delete(supplier)
    }

    fun getSupplierById(id: Int): LiveData<Supplier> {
        return repository.getSupplierById(id)
    }
}