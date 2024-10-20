package com.tugas.aplikasimonitoringgudang.data.user

import androidx.room.*
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg user: User)

    @Update
    suspend fun update(vararg user: User)

    @Delete
    suspend fun delete(vararg user: User)

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    @Query("SELECT * FROM user_table WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): User?
}