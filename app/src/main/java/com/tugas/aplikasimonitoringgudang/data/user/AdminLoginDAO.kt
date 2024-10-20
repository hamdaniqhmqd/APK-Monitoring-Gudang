package com.tugas.aplikasimonitoringgudang.data.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.lifecycle.LiveData

@Dao
interface UserDao {
    @Insert
    suspend fun insert(vararg user: User)

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    @Query("SELECT * FROM user_table WHERE username = :username")
    fun getAdminLiveData(username: String): LiveData<User?>
}
