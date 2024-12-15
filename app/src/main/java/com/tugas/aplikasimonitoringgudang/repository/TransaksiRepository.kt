package com.tugas.aplikasimonitoringgudang.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.tugas.aplikasimonitoringgudang.api.ApiTransaksiService
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.api.RetrofitInstanceGudangApi
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.local.TransaksiDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class TransaksiRepository(
    private val transaksiDao: TransaksiDao,
    private val networkHelper: NetworkHelper
) {
    private val apiService: ApiTransaksiService = RetrofitInstanceGudangApi.apiTransaksiService

    // Fungsi sinkronisasi insert data
    private suspend fun sinkronisasiDataTransaksiInsert(
        apiData: List<Transaksi>,
        localData: List<Transaksi>
    ) {
        // Sinkronisasi data dari API ke lokal
        val dataApiToLocal = apiData.filter { apiItem ->
            localData.none { it.id_transaksi == apiItem.id_transaksi }
        }
        dataApiToLocal.forEach { transaksiDao.insert(it) }

        // Sinkronisasi data dari lokal ke API
        val dataLocalToApi = localData.filter { localItem ->
            apiData.none { it.id_transaksi == localItem.id_transaksi }
        }
        dataLocalToApi.forEach { transaksi ->
            try {
                val response = apiService.addTransaksi(transaksi)
                if (!response.success) {
                    Log.e("MyAppError", "Gagal menambahkan transaksi ke API: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("MyAppError", "Error saat menyinkronkan transaksi ke API: ${e.message}")
            }
        }
    }

    // Fungsi sinkronisasi update data
    private suspend fun sinkronisasiDataTransaksiUpdate(
        apiData: List<Transaksi>,
        localData: List<Transaksi>
    ) {
        // Update data lokal berdasarkan data dari API jika ada perubahan
        val updatedApiData = apiData.filter { apiItem ->
            localData.any { localItem ->
                localItem.id_transaksi == apiItem.id_transaksi && localItem != apiItem
            }
        }
        updatedApiData.forEach { transaksi ->
            try {
                transaksiDao.update(transaksi)
                val response = apiService.updateTransaksi(transaksi.id_transaksi, transaksi)
                if (!response.success) {
                    Log.e("MyAppError", "Gagal memperbarui transaksi di API: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("MyAppError", "Error saat memperbarui transaksi di API: ${e.message}")
            }
        }

        // Update data di API berdasarkan data dari lokal jika ada perubahan
        val updatedLocalData = localData.filter { localItem ->
            apiData.any { apiItem ->
                apiItem.id_transaksi == localItem.id_transaksi && apiItem != localItem
            }
        }
        updatedLocalData.forEach { transaksi ->
            try {
                transaksiDao.update(transaksi)
                val response = apiService.updateTransaksi(transaksi.id_transaksi, transaksi)
                if (!response.success) {
                    Log.e("MyAppError", "Gagal memperbarui transaksi di API: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("MyAppError", "Error saat memperbarui transaksi di API: ${e.message}")
            }
        }
    }

    private suspend fun sinkronisasiDataTransaksiDelete(
        apiData: List<Transaksi>,
        localData: List<Transaksi>
    ) {
//        // Data yang ada di API tetapi sudah tidak ada di lokal
//        val dataApi = apiData.filter { apiItem ->
//            localData.none { localItem -> localItem.id_transaksi == apiItem.id_transaksi }
//        }
//        // Hapus data dari API
//        dataApi.forEach {
//            try {
//                val response = apiService.deleteTransaksi(it.id_transaksi)
//                if (!response.success) {
//                    throw Exception("Failed to delete transaksi from API: ${response.message}")
//                }
//            } catch (e: Exception) {
//                // Log atau tangani error jika penghapusan di API gagal
//                e.printStackTrace()
//            }
//        }

        // Data yang ada di lokal tetapi sudah tidak ada di API
        val dataLocal = localData.filter { localItem ->
            apiData.none { apiItem -> apiItem.id_transaksi == localItem.id_transaksi }
        }
        // Hapus data dari lokal
        dataLocal.forEach {
            transaksiDao.delete(it)
        }
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
//                        sinkronisasiDataTransaksiDelete(apiData, localData)
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
    suspend fun getTransaksiById(id: Long): Transaksi {
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
    suspend fun updateTransaksi(id: Long, transaksi: Transaksi): Transaksi {
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

