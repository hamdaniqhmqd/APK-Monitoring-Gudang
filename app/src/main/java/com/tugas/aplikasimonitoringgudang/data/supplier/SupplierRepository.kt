package com.tugas.aplikasimonitoringgudang.data.supplier

import androidx.lifecycle.LiveData
import com.tugas.aplikasimonitoringgudang.data.barang.BarangDao
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier

class SupplierRepository(private val supplierDao: SupplierDao) {
    val allSupplier: LiveData<List<Supplier>> = supplierDao.getAllSupplier()

    suspend fun insert(supplier: Supplier) {
        supplierDao.insert(supplier)
    }

    suspend fun update(supplier: Supplier){
        supplierDao.update(supplier)
    }

    suspend fun delete(supplier: Supplier){
        supplierDao.delete(supplier)
    }

    fun getSupplierById(id: Int): LiveData<Supplier> {
        return supplierDao.getSupplierById(id)
    }

    suspend fun getSupplierCount(): Int = supplierDao.getSupplierCount()
}
