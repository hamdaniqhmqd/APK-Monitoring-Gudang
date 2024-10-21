package com.tugas.aplikasimonitoringgudang.data.user

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
//    val allUser: LiveData<List<User>> = userDao.getUser()

    suspend fun insert(transaksi: User) {
        userDao.insert(transaksi)
    }

    suspend fun update(transaksi: User) {
        userDao.update(transaksi)
    }

    suspend fun delete(transaksi: User) {
        userDao.delete(transaksi)
    }

    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }

    fun getAdminLiveData(username: String): LiveData<User?> {
        return userDao.getAdminLiveData(username)
    }
}
