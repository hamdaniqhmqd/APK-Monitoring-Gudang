package com.tugas.aplikasimonitoringgudang.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.tugas.aplikasimonitoringgudang.api.ApiTransaksiService
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.api.RetrofitInstanceGudangApi
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.local.TransaksiDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class TransaksiRepository(
    private val transaksiDao: TransaksiDao,
    private val networkHelper: NetworkHelper
) {
    private val apiService: ApiTransaksiService = RetrofitInstanceGudangApi.apiTransaksiService

    private suspend fun sinkronisasiDataTransaksiInsert(
        apiData: List<Transaksi>,
        localData: List<Transaksi>
    ) {
//        // Data yang hanya ada di API dan belum ada di lokal
//        val dataApi = apiData.filter { apiItem ->
//            localData.none { localItem -> localItem.id_transaksi == apiItem.id_transaksi }
//        }
//        // Insert data baru dari API ke lokal
//        dataApi.forEach { transaksiDao.insert(it) }

        // Data yang hanya ada di lokal dan belum ada di API
        val dataLokal = localData.filter { localItem ->
            apiData.none { apiItem -> apiItem.id_transaksi == localItem.id_transaksi }
        }
        // Insert data baru dari lokal ke API
        dataLokal.forEach {
            try {
                val response = apiService.addTransaksi(it)
                if (!response.success) {
                    throw Exception("Failed to sync local data to API: ${response.message}")
                }
            } catch (e: Exception) {
                // Log atau tangani error jika sinkronisasi ke API gagal
                e.printStackTrace()
            }
        }
    }

    private suspend fun sinkronisasiDataTransaksiUpdate (
        apiData: List<Transaksi>,
        localData: List<Transaksi>
    ) {
//        // Data yang ada di kedua sumber tetapi mungkin berbeda (harus di-update)
//        val dataApi = apiData.filter { apiItem ->
//            localData.any { localItem ->
//                localItem.id_transaksi == apiItem.id_transaksi && localItem != apiItem
//            }
//        }
//        // Update data lokal jika ada perubahan dari API
//        dataApi.forEach { updatedItem ->
//            transaksiDao.update(updatedItem)
//        }

        // Update data API jika ada perubahan dari lokal
        val dataLocal = localData.filter { localItem ->
            apiData.any { apiItem ->
                apiItem.id_transaksi == localItem.id_transaksi && apiItem != localItem
            }
        }
        dataLocal.forEach {
            try {
                val response = apiService.updateTransaksi(it.id_transaksi, it)
                if (!response.success) {
                    throw Exception("Failed to update transaksi on API: ${response.message}")
                }
            } catch (e: Exception) {
                // Log atau tangani error jika update ke API gagal
                e.printStackTrace()
            }
        }
    }

    private suspend fun sinkronisasiDataTransaksiDelete (
        apiData: List<Transaksi>,
        localData: List<Transaksi>
    ) {
        // Data yang ada di API tetapi sudah tidak ada di lokal
        val dataApi = apiData.filter { apiItem ->
            localData.none { localItem -> localItem.id_transaksi == apiItem.id_transaksi }
        }
        // Hapus data dari API
        dataApi.forEach {
            try {
                val response = apiService.deleteTransaksi(it.id_transaksi)
                if (!response.success) {
                    throw Exception("Failed to delete transaksi from API: ${response.message}")
                }
            } catch (e: Exception) {
                // Log atau tangani error jika penghapusan di API gagal
                e.printStackTrace()
            }
        }

//        // Data yang ada di lokal tetapi sudah tidak ada di API
//        val dataLocal = localData.filter { localItem ->
//            apiData.none { apiItem -> apiItem.id_transaksi == localItem.id_transaksi }
//        }
//        // Hapus data dari lokal
//        dataLocal.forEach {
//            transaksiDao.delete(it)
//        }
    }


    // Fungsi untuk mengambil semua transaksi
    suspend fun getAllTransaksi(): List<Transaksi> {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getTransaksi()
                    if (response.success) {
                        val apiData = response.data
                        val localData = transaksiDao.getAllTransaksi()

                        // Sinkronisasi data antara API ke lokal
                        sinkronisasiDataTransaksiInsert(apiData, localData)
                        sinkronisasiDataTransaksiUpdate(apiData, localData)
                        sinkronisasiDataTransaksiDelete(apiData, localData)
                        // Mengembalikan data transaksi yang diterima dari API
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to fetch transaksi list: ${response.message}")

                    }
                } catch (e: Exception) {
                    // Jika error, gunakan data lokal
                    transaksiDao.getAllTransaksi()
                }
            } else {
                // Jika tidak ada koneksi, gunakan data lokal
                transaksiDao.getAllTransaksi()
            }
        }
    }

    // Fungsi untuk mengambil transaksi berdasarkan ID
    suspend fun getTransaksiById(id: Int): Transaksi {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getTransaksiById(id)
                    if (response.success) {
//                        getAllTransaksi()
                        // Mengembalikan detail transaksi yang diterima
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to fetch transaksi detail: ${response.message}")
                    }
                } catch (e: Exception) {
                    // Jika API error, ambil data lokal
                    transaksiDao.getTransaksiById(id)
                }
            } else {
                // Gunakan data lokal jika offline
                transaksiDao.getTransaksiById(id)
            }
        }
    }

    // Fungsi untuk menambah transaksi
    suspend fun addTransaksi(transaksi: Transaksi): Transaksi {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    Log.d("Transaksi", "Sending data to API: $transaksi")
                    val response = apiService.addTransaksi(transaksi)
                    if (response.success) {
                        transaksiDao.insert(response.data)
//                        getAllTransaksi()
                        // Mengembalikan transaksi yang baru ditambahkan
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to add transaksi: ${response.message}")
                    }
                } catch (e: Exception) {
                    Log.e("AddTransaksi", "Unknown Error: ${e.message}")
                    transaksiDao.insert(transaksi)
                    transaksi
                }
            } else {
                Log.w("AddTransaksi", "Offline mode, saving only to local DB.")
                transaksiDao.insert(transaksi)
                transaksi
            }
        }
    }


    // Fungsi untuk memperbarui transaksi
    suspend fun updateTransaksi(id: Int, transaksi: Transaksi): Transaksi {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.updateTransaksi(id, transaksi)
                    if (response.success) {

//                        getAllTransaksi()
                        // Perbarui di lokal
                        transaksiDao.update(response.data)
                        // Mengembalikan transaksi yang sudah diperbarui
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to update transaksi: ${response.message}")
                    }
                } catch (e: Exception) {
                    // Jika error, hanya perbarui di lokal
                    transaksiDao.update(transaksi)
                    transaksi
                }
            } else {
                // Perbarui hanya di lokal jika offline
                transaksiDao.update(transaksi)
                transaksi
            }
        }
    }

    // Fungsi untuk menghapus transaksi
    suspend fun deleteTransaksi(transaksi: Transaksi) {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.deleteTransaksi(transaksi.id_transaksi)
                    if (response.success) {
//                        getAllTransaksi()
                        // Hapus dari lokal
                        transaksiDao.delete(transaksi)
                    } else {
                        throw Exception("Failed to delete transaksi: ${response.message}")
                    }
                } catch (e: Exception) {
                    // Jika error, tetap hapus dari lokal
                    transaksiDao.delete(transaksi)
                }
            } else {
                // Hapus hanya di lokal jika offline
                transaksiDao.delete(transaksi)
            }
        }
    }
}

