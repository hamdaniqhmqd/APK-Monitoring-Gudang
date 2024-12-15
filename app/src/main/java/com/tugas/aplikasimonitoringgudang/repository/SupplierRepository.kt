package com.tugas.aplikasimonitoringgudang.repository

import android.util.Log
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
        // Sinkronisasi data dari API ke lokal
        val dataApiToLocal = apiData.filter { apiItem ->
            localData.none { it.id_supplier == apiItem.id_supplier }
        }
        dataApiToLocal.forEach { supplierDao.insert(it) }

        // Sinkronisasi data dari lokal ke API
        val dataLocalToApi = localData.filter { localItem ->
            apiData.none { it.id_supplier == localItem.id_supplier }
        }
        dataLocalToApi.forEach { supplier ->
            try {
                val response = apiService.addSupplier(supplier)
                if (!response.success) {
                    Log.e( "MyAppError","Gagal menambahkan supplier ke API: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e( "MyAppError","Error saat menyinkronkan supplier ke API: ${e.message}")
            }
        }
    }

    private suspend fun sinkronisasiDataSupplierUpdate(
        apiData: List<Supplier>,
        localData: List<Supplier>
    ) {
        // Update data lokal berdasarkan data dari API jika ada perubahan
        val updatedApiToLocal = apiData.filter { apiItem ->
            localData.any { localItem ->
                localItem.id_supplier == apiItem.id_supplier && localItem != apiItem
            }
        }
        updatedApiToLocal.forEach { supplier ->
            try {
                supplierDao.update(supplier)
                val response = apiService.updateSupplier(supplier.id_supplier, supplier)
                if (!response.success) {
                    Log.e("MyAppError", "Gagal memperbarui supplier di API: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("MyAppError", "Error saat memperbarui supplier di API: ${e.message}")
            }
        }

        // Update data di API berdasarkan data dari lokal jika ada perubahan
        val updatedLocalToApi = localData.filter { localItem ->
            apiData.any { apiItem ->
                apiItem.id_supplier == localItem.id_supplier && apiItem != localItem
            }
        }

        updatedLocalToApi.forEach { supplier ->
            try {
                supplierDao.update(supplier)
                val response = apiService.updateSupplier(supplier.id_supplier, supplier)
                if (!response.success) {
                    Log.e("MyAppError", "Gagal memperbarui supplier di API: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("MyAppError", "Error saat memperbarui supplier di API: ${e.message}")
            }
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
//                        sinkronisasiDataSupplierDelete(apiData, localData)
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
    suspend fun getSupplierById(id: Long): Supplier {
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
    suspend fun update(id: Long, supplier: Supplier): Supplier {
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

