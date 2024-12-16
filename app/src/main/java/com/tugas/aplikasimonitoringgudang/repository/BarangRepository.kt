package com.tugas.aplikasimonitoringgudang.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tugas.aplikasimonitoringgudang.api.ApiBarangService
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.api.RetrofitInstanceGudangApi
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.database.GudangDatabase
import com.tugas.aplikasimonitoringgudang.local.BarangDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//repository
class BarangRepository(
    private val barangDao: BarangDao,
    private val networkHelper: NetworkHelper
) {
    //API service
    private val apiService: ApiBarangService = RetrofitInstanceGudangApi.apiBarangService

    private suspend fun sinkronisasiDataBarangInsert(
        apiData: List<Barang>,
        localData: List<Barang>
    ) {
        // Sinkronisasi data dari API ke lokal
        val dataApiToLocal = apiData.filter { apiItem ->
            localData.none { it.id_barang == apiItem.id_barang }
        }
        dataApiToLocal.forEach { barang ->
            barangDao.insert(barang)
        }

        // Sinkronisasi data dari lokal ke API
        val dataLocalToApi = localData.filter { localItem ->
            apiData.none { it.id_barang == localItem.id_barang }
        }
        dataLocalToApi.forEach { barang ->
            try {
                val response = apiService.addBarang(barang)
                if (!response.success) {
                    Log.e("MyAppError", "Gagal menambahkan barang ke API: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("MyAppError", "Error saat menyinkronkan barang ke API: ${e.message}")
            }
        }
    }

    private suspend fun sinkronisasiDataBarangUpdate(
        apiData: List<Barang>,
        localData: List<Barang>
    ) {
        // Update data lokal berdasarkan data dari API jika ada perubahan
        val updatedApiToLocal = apiData.filter { apiItem ->
            localData.any { localItem ->
                localItem.id_barang == apiItem.id_barang && localItem != apiItem
            }
        }
        updatedApiToLocal.forEach { barang ->
            try {
                barangDao.update(barang)
                val response = apiService.updateBarang(barang.id_barang, barang)
                if (!response.success) {
                    Log.e("MyAppError", "Gagal memperbarui barang di API: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("MyAppError", "Error saat memperbarui barang di API: ${e.message}")
            }
        }

        // Update data di API berdasarkan data dari lokal jika ada perubahan
        val updatedLocalToApi = localData.filter { localItem ->
            apiData.any { apiItem ->
                apiItem.id_barang == localItem.id_barang && apiItem != localItem
            }
        }
        updatedLocalToApi.forEach { barang ->
            try {
                barangDao.update(barang)
                val response = apiService.updateBarang(barang.id_barang, barang)
                if (!response.success) {
                    Log.e("MyAppError", "Gagal memperbarui barang di API: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("MyAppError", "Error saat memperbarui barang di API: ${e.message}")
            }
        }
    }

    //mendefinisikan coroutines (suspen). mengembalikan data list barang dan menampilkannya
    suspend fun getAllBarang(): List<Barang> {
        return withContext(Dispatchers.IO) {
            //class mengecek internet apakah true/false
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getBarang()
                    if (response.success) {
                        val apiData = response.data
                        val localData = barangDao.getAllBarang()

                        // Sinkronisasi data
                        sinkronisasiDataBarangInsert(apiData, localData)
                        sinkronisasiDataBarangUpdate(apiData, localData)
//                        sinkronisasiDataBarangDelete(apiData, localData)
                        return@withContext apiData
                    } else {
                        throw Exception("Failed to fetch barang list: ${response.message}")
                    }
                } catch (e: Exception) {
                    // Jika terjadi error, gunakan data lokal
                    barangDao.getAllBarang()
                }
            } else {
                // Gunakan data lokal jika tidak ada koneksi
                barangDao.getAllBarang()
            }
        }
    }

    //
    suspend fun getBarangById(id: Long): Barang {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getBarangById(id)
                    if (response.success) {
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to fetch barang detail: ${response.message}")
                    }
                } catch (e: Exception) {
                    // Jika API error, ambil data lokal
                    barangDao.getBarangById(id)
                }
            } else {
                // Gunakan data lokal jika offline
                barangDao.getBarangById(id)
            }
        }
    }

    //
    suspend fun insert(barang: Barang): Barang {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.addBarang(barang)
                    if (response.success) {

                        return@withContext response.data // Mengembalikan ID barang sebagai Long
                    } else {
                        throw Exception("Failed to add barang: ${response.message}")
                    }
                } catch (e: Exception) {
                    barangDao.insert(barang)
                    barang
                }
            } else {
                barangDao.insert(barang)
                barang
            }
        }
    }

    suspend fun update(id: Long, barang: Barang): Barang {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.updateBarang(id, barang)
                    if (response.success) {
                        barangDao.update(response.data)
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to update barang: ${response.message}")
                    }
                } catch (e: Exception) {
                    barangDao.update(barang)
                    barang
                }
            } else {
                barangDao.update(barang)
                barang
            }
        }
    }


    suspend fun delete(barang: Barang) {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.deleteBarang(barang.id_barang)
                    if (response.success) {
                        barangDao.delete(barang)
                    } else {
                        throw Exception("Failed to delete barang: ${response.message}")
                    }
                } catch (e: Exception) {
                    barangDao.delete(barang)
                }
            } else {
                barangDao.delete(barang)
            }
        }
    }

    suspend fun getBarangBySupplierId(supplierId: Long): List<Barang> {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getBarangByIdSupplier(supplierId)
                    if (response.success) {
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to fetch barang by supplier: ${response.message}")
                    }
                } catch (e: Exception) {
                    // Ambil data barang yang sudah ada di lokal
                    barangDao.getBarangByIdSuppler(supplierId)
                }
            } else {
                // Jika offline, ambil data lokal
                barangDao.getBarangByIdSuppler(supplierId)
            }
        }
    }
}
