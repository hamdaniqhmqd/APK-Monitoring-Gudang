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

class UserRepository(
    private val userDao: UserDao,
    private val networkHelper: NetworkHelper
) {
    private val apiService: ApiUserService = RetrofitInstanceGudangApi.apiUserService

    suspend fun sinkronisasiDataUserInsert(
        apiData: List<User>,
        localData: List<User>
    ) {
        // Data yang hanya ada di lokal dan belum ada di API
        val dataLokal = localData.filter { localItem ->
            apiData.none { apiItem -> apiItem.id == localItem.id }
        }
        // Insert data baru dari lokal ke API
        dataLokal.forEach {
            try {
                val response = apiService.createAdmin(it)
                if (!response.success) {
                    throw Exception("Failed to sync local data to API: ${response.message}")
                }
            } catch (e: Exception) {
                // Log atau tangani error jika sinkronisasi ke API gagal
                e.printStackTrace()
            }
        }
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
                        val localUser = userDao.getUser(user.username, user.password)
                        userDao.getUser(user.username, user.password)
                        return@withContext localUser
                    } else {
                        throw Exception("Failed to login: ${response.message}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    userDao.getUser(user.username, user.password)
                }
            } else {
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
                        userDao.insert(response.data)
//                        getAllTransaksi()
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

    suspend fun update(user: User): User {
        return withContext(Dispatchers.IO) {
            if (networkHelper.isConnected()) {
                try {
                    val response = apiService.updateAdmin(user.id, user)
                    if (response.success) {
                        userDao.update(response.data)
//                        getAllTransaksi()
                        // Mengembalikan transaksi yang baru ditambahkan
                        return@withContext response.data
                    } else {
                        throw Exception("Failed to add transaksi: ${response.message}")
                    }
                } catch (e: Exception) {
                    userDao.update(user)
                    user
                }
            } else {
                userDao.update(user)
                user
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
