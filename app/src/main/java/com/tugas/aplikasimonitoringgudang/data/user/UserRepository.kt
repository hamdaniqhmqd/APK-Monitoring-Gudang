package com.tugas.aplikasimonitoringgudang.data.user

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    fun getAdminLiveData(username: String): LiveData<User?> {
        return userDao.getAdminLiveData(username)
    }

    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}
