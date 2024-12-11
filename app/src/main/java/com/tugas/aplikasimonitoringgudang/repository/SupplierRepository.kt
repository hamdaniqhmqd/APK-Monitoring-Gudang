package com.tugas.aplikasimonitoringgudang.repository

import androidx.lifecycle.LiveData
import com.tugas.aplikasimonitoringgudang.api.ApiSupplierService
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.api.RetrofitInstanceGudangApi
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.local.SupplierDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SupplierRepository(
    private val supplierDao: SupplierDao,
    private val networkHelper: NetworkHelper
) {
    private val apiService: ApiSupplierService = RetrofitInstanceGudangApi.apiSupplierService

    private suspend fun sinkronisasiDataSupplierInsert(
        apiData: List<Supplier>,
        localData: List<Supplier>
    ) {
        // Data yang hanya ada di API dan belum ada di lokal
        val dataApi = apiData.filter { apiItem ->
            localData.none { localItem -> localItem.id_supplier == apiItem.id_supplier }
        }
        // Insert data baru dari API ke lokal
        dataApi.forEach { supplierDao.insert(it) }
    }

    private suspend fun sinkronisasiDataSupplierUpdate(
        apiData: List<Supplier>,
        localData: List<Supplier>
    ) {
        // Data yang ada di kedua sumber tetapi mungkin berbeda (harus di-update)
        val dataApi = apiData.filter { apiItem ->
            localData.any { localItem ->
                localItem.id_supplier == apiItem.id_supplier && localItem != apiItem
            }
        }
        // Update data lokal jika ada perubahan dari API
        dataApi.forEach { updatedItem ->
            supplierDao.update(updatedItem)
        }
    }

    private suspend fun sinkronisasiDataSupplierDelete(
        apiData: List<Supplier>,
        localData: List<Supplier>
    ) {
        // Data yang ada di lokal tetapi sudah tidak ada di API
        val dataLocal = localData.filter { localItem ->
            apiData.none { apiItem -> apiItem.id_supplier == localItem.id_supplier }
        }
        // Hapus data dari lokal
        dataLocal.forEach {
            supplierDao.delete(it)
        }
    }

    // Fungsi untuk mengambil semua supplier
    suspend fun getAllSupplier(): List<Supplier> {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getSupplier()
                    if (response.success) {
                        val apiData = response.data
                        val localData = supplierDao.getAllSupplier()

                        // Sinkronisasi data antara API ke lokal
                        sinkronisasiDataSupplierInsert(apiData, localData)
                        sinkronisasiDataSupplierUpdate(apiData, localData)
                        sinkronisasiDataSupplierDelete(apiData, localData)
                        // Mengembalikan data supplier yang diterima dari API
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to fetch supplier list: ${response.message}")
                    }
                } catch (e: Exception) {
                    // Jika error, gunakan data lokal
                    supplierDao.getAllSupplier()
                }
            } else {
                // Jika tidak ada koneksi, gunakan data lokal
                supplierDao.getAllSupplier()
            }
        }
    }

    // Fungsi untuk mengambil supplier berdasarkan ID
    suspend fun getSupplierById(id: Int): Supplier {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getSupplierById(id)
                    if (response.success) {
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to fetch supplier detail: ${response.message}")
                    }
                } catch (e: Exception) {
                    // Jika API error, ambil data lokal
                    supplierDao.getSupplierById(id)
                }
            } else {
                // Gunakan data lokal jika offline
                supplierDao.getSupplierById(id)
            }
        }
    }

    // Fungsi untuk menambah supplier
    suspend fun insert(supplier: Supplier): Supplier {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.addSupplier(supplier)
                    if (response.success) {
                        supplierDao.insert(response.data)
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to add supplier: ${response.message}")
                    }
                } catch (e: Exception) {
                    supplierDao.insert(supplier)
                    supplier
                }
            } else {
                supplierDao.insert(supplier)
                supplier
            }
        }
    }

    // Fungsi untuk memperbarui supplier
    suspend fun update(id: Int, supplier: Supplier): Supplier {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.updateSupplier(id, supplier)
                    if (response.success) {
                        supplierDao.update(response.data)
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to update supplier: ${response.message}")
                    }
                } catch (e: Exception) {
                    supplierDao.update(supplier)
                    supplier
                }
            } else {
                supplierDao.update(supplier)
                supplier
            }
        }
    }

    // Fungsi untuk menghapus supplier
    suspend fun delete(supplier: Supplier) {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.deleteSupplier(supplier.id_supplier)
                    if (response.success) {
                        supplierDao.delete(supplier)
                    } else {
                        throw Exception("Failed to delete supplier: ${response.message}")
                    }
                } catch (e: Exception) {
                    supplierDao.delete(supplier)
                }
            } else {
                supplierDao.delete(supplier)
            }
        }
    }
}

