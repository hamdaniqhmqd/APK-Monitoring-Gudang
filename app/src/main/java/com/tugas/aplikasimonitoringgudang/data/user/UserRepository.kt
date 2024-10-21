package com.tugas.aplikasimonitoringgudang.data.user

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
//    val allUser: LiveData<List<User>> = userDao.getUser()

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }
    
    fun getAdminLiveData(username: String): LiveData<User?> {
        return userDao.getAdminLiveData(username)
    }
}
