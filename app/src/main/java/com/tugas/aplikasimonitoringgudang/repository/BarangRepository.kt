package com.tugas.aplikasimonitoringgudang.repository
import android.app.Application
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

class BarangRepository(
    private val barangDao: BarangDao,
    private val networkHelper: NetworkHelper
) {
    private val apiService: ApiBarangService = RetrofitInstanceGudangApi.apiBarangService

    private suspend fun sinkronisasiDataBarangInsert(
        apiData: List<Barang>,
        localData: List<Barang>
    ) {
        val dataApi = apiData.filter { apiItem ->
            localData.none { localItem -> localItem.id_barang == apiItem.id_barang }
        }
        dataApi.forEach { barangDao.insert(it) }
    }

    private suspend fun sinkronisasiDataBarangUpdate(
        apiData: List<Barang>,
        localData: List<Barang>
    ) {
        val dataApi = apiData.filter { apiItem ->
            localData.any { localItem ->
                localItem.id_barang == apiItem.id_barang && localItem != apiItem
            }
        }
        dataApi.forEach { updatedItem -> barangDao.update(updatedItem) }
    }

    private suspend fun sinkronisasiDataBarangDelete(
        apiData: List<Barang>,
        localData: List<Barang>
    ) {
        val dataLocal = localData.filter { localItem ->
            apiData.none { apiItem -> apiItem.id_barang == localItem.id_barang }
        }
        dataLocal.forEach { barangDao.delete(it) }
    }

    suspend fun getAllBarang(): List<Barang> {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getBarang()
                    if (response.success) {
                        val apiData = response.data
                        val localData = barangDao.getAllBarang()

                        // Sinkronisasi data
                        sinkronisasiDataBarangInsert(apiData, localData)
                        sinkronisasiDataBarangUpdate(apiData, localData)
                        sinkronisasiDataBarangDelete(apiData, localData)
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

    suspend fun getBarangById(id: Int): Barang {
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

    suspend fun insert(barang: Barang): Barang {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.addBarang(barang)
                    if (response.success) {
                        barangDao.insert(response.data)
                        return@withContext response.data
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

    suspend fun update(id: Int, barang: Barang): Barang {
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

    suspend fun getBarangBySupplierId(supplierId: Int): List<Barang> {
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
