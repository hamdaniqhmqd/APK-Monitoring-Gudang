package com.tugas.aplikasimonitoringgudang.data.user

import androidx.lifecycle.LiveData
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi

class UserRepository(private val userDao: UserDao) {
    // Metode CRUD lainnya...

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

    fun getAdminLiveData(username: String): LiveData<User?> {
        return userDao.getUserByUsername(username)
    }

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    fun getUserById(id: Int): LiveData<User> {
        return userDao.getUserById(id)
    }

    suspend fun updateAdminProfile(username: String, adminName: String, profileImagePath: String) {
        userDao.updateAdminProfile(username, adminName, profileImagePath)
    }

    fun getAdminName(username: String): LiveData<String> {
        return userDao.getAdminName(username)
    }
}
