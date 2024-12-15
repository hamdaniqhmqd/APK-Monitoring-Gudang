package com.tugas.aplikasimonitoringgudang.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.data.user.User

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

    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getUserById(id: Long): User

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    suspend fun getAllUser(): List<User>

    @Query("SELECT COUNT(*) FROM barang_table")
    suspend fun getBarangCount(): Int

    @Query("SELECT COUNT(*) FROM supplier_table")
    suspend fun getSupplierCount(): Int

    @Query("SELECT COUNT(*) FROM transaksi_table WHERE status = 2")
    suspend fun getTransaksiMasukCount(): Int

    @Query("SELECT COUNT(*) FROM transaksi_table WHERE status = 1")
    suspend fun getTransaksiKeluarCount(): Int

    @Query("SELECT * FROM user_table WHERE username = :username")
    fun getUserByUsername(username: String): LiveData<User?>
}
