package com.tugas.aplikasimonitoringgudang.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.tugas.aplikasimonitoringgudang.api.ApiUserService
import com.tugas.aplikasimonitoringgudang.api.NetworkHelper
import com.tugas.aplikasimonitoringgudang.api.RetrofitInstanceGudangApi
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.local.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepository(
    private val userDao: UserDao,
    private val networkHelper: NetworkHelper
) {
    private val apiService: ApiUserService = RetrofitInstanceGudangApi.apiUserService

    private suspend fun sinkronisasiDataUserInsert(
        apiData: List<User>,
        localData: List<User>
    ) {
        // Data yang hanya ada di API dan belum ada di lokal
        val dataApi = apiData.filter { apiItem ->
            localData.none { localItem -> localItem.id == apiItem.id }
        }
        // Insert data baru dari API ke lokal
        dataApi.forEach { userDao.insert(it) }

//        // Data yang hanya ada di lokal dan belum ada di API
//        val dataLokal = localData.filter { localItem ->
//            apiData.none { apiItem -> apiItem.id == localItem.id }
//        }
//        // Insert data baru dari lokal ke API
//        dataLokal.forEach {
//            try {
//                val response = apiService.createAdmin(it)
//                if (!response.success) {
//                    throw Exception("Failed to sync local data to API: ${response.message}")
//                }
//            } catch (e: Exception) {
//                // Log atau tangani error jika sinkronisasi ke API gagal
//                e.printStackTrace()
//            }
//        }
    }

    private suspend fun sinkronisasiDataUserUpdate(
        apiData: List<User>,
        localData: List<User>
    ) {
        // Data yang ada di lokal tetapi sudah tidak ada di API
        val dataLocal = localData.filter { localItem ->
            apiData.none { apiItem -> apiItem.id == localItem.id  && localItem != apiItem}
        }
        // Hapus data dari lokal
        dataLocal.forEach {
            userDao.update(it)
        }

//        // Data yang hanya ada di lokal dan belum ada di API
//        val dataLokal = localData.filter { localItem ->
//            apiData.none { apiItem -> apiItem.id == localItem.id }
//        }
//        // Insert data baru dari lokal ke API
//        dataLokal.forEach {
//            try {
//                val response = apiService.createAdmin(it)
//                if (!response.success) {
//                    throw Exception("Failed to sync local data to API: ${response.message}")
//                }
//            } catch (e: Exception) {
//                // Log atau tangani error jika sinkronisasi ke API gagal
//                e.printStackTrace()
//            }
//        }
    }

    private suspend fun sinkronisasiDataUserDelete(
        apiData: List<User>,
        localData: List<User>
    ) {
        // Data yang ada di lokal tetapi sudah tidak ada di API
        val dataLocal = localData.filter { localItem ->
            apiData.none { apiItem -> apiItem.id == localItem.id }
        }
        // Hapus data dari lokal
        dataLocal.forEach {
            userDao.delete(it)
        }

//        // Data yang hanya ada di lokal dan belum ada di API
//        val dataLokal = localData.filter { localItem ->
//            apiData.none { apiItem -> apiItem.id == localItem.id }
//        }
//        // Insert data baru dari lokal ke API
//        dataLokal.forEach {
//            try {
//                val response = apiService.createAdmin(it)
//                if (!response.success) {
//                    throw Exception("Failed to sync local data to API: ${response.message}")
//                }
//            } catch (e: Exception) {
//                // Log atau tangani error jika sinkronisasi ke API gagal
//                e.printStackTrace()
//            }
//        }
    }

    suspend fun sinkronisasiDataUser(): List<User> {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getAllAdmins()
                    if (response.success) {
                        val apiData = response.data
                        val localData = userDao.getAllUser()

                        // Sinkronisasi data antara API ke lokal
                        sinkronisasiDataUserInsert(apiData, localData)
                        sinkronisasiDataUserUpdate(apiData, localData)
//                        sinkronisasiDataUserDelete(apiData, localData)
                        // Mengembalikan data transaksi yang diterima dari API
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to fetch transaksi list: ${response.message}")

                    }
                } catch (e: Exception) {
                    // Jika error, gunakan data lokal
                    userDao.getAllUser()
                }
            } else {
                // Jika tidak ada koneksi, gunakan data lokal
                userDao.getAllUser()
            }
        }
    }

    suspend fun login(user: User): User? {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.loginAdmin(user)
                    if (response.success) {
                        return@withContext response.data
                    } else {
                        throw Exception("Login failed: ${response.message}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Fallback ke database lokal
                    userDao.getUser(user.username, user.password)
                }
            } else {
                // Fallback ke database lokal jika tidak ada koneksi
                userDao.getUser(user.username, user.password)
            }
        }
    }



    // Metode CRUD lainnya...
    suspend fun insert(user: User): User {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.createAdmin(user)
                    if (response.success) {
                        // Mengembalikan transaksi yang baru ditambahkan
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to add transaksi: ${response.message}")
                    }
                } catch (e: Exception) {
                    Log.e("AddTransaksi", "Unknown Error: ${e.message}")
                    userDao.insert(user)
                    user
                }
            } else {
                userDao.insert(user)
                user
            }
        }
    }

    suspend fun update(user: User, profileImagePath: MultipartBody.Part?): User {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val usernameBody = user.username.toRequestBody("text/plain".toMediaTypeOrNull())
                    val passwordBody = user.password.toRequestBody("text/plain".toMediaTypeOrNull())
                    val adminNameBody = user.adminName.toRequestBody("text/plain".toMediaTypeOrNull())
                    val requestMethod = "PUT".toRequestBody("text/plain".toMediaTypeOrNull())

                    val response = apiService.updateAdmin(
                        user.id,
                        usernameBody,
                        passwordBody,
                        adminNameBody,
                        profileImagePath,
                        requestMethod
                    )

                    if (response.success) {
                        userDao.update(response.data)
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to update user: ${response.message}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext user
                }
            } else {
                throw Exception("No internet connection")
            }
        }
    }

    suspend fun delete(user: User) {
        withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.deleteAdmin(user.id)
                    if (response.success) {
                        userDao.delete(response.data)
                    } else {
                        throw Exception("Failed to delete user: ${response.message}")
                    }
                } catch (e: Exception) {
                    userDao.delete(user)
                    user
                }
            } else {
                userDao.delete(user)
                user
            }
        }
    }

    suspend fun getUserById(id: Int): User {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.getAdminById(id)
                    if (response.success) {
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to fetch user detail: ${response.message}")
                    }
                } catch (e: Exception) {
                    userDao.getUserById(id)
                }
            } else {
                userDao.getUserById(id)
            }
        }
    }

    suspend fun getBarangCount(): Int {
        return userDao.getBarangCount()
    }

    suspend fun getSupplierCount(): Int {
        return userDao.getSupplierCount()
    }

    suspend fun getTransaksiMasukCount(): Int {
        return userDao.getTransaksiMasukCount()
    }

    suspend fun getTransaksiKeluarCount(): Int {
        return userDao.getTransaksiKeluarCount()
    }
}
