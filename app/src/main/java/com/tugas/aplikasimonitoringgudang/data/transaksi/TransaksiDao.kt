package com.tugas.aplikasimonitoringgudang.data.transaksi

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransaksiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg transaksi: Transaksi)

    @Update
    suspend fun update(vararg transaksi: Transaksi)

    @Delete
    suspend fun delete(vararg transaksi: Transaksi)

    @Query("SELECT * FROM transaksi_table ORDER BY id_transaksi ASC")
    fun getAllTransaksi(): LiveData<List<Transaksi>>

    @Query("SELECT * FROM transaksi_table WHERE id_transaksi = :id LIMIT 1")
    fun getBarangById(id: Int): LiveData<Transaksi>
}
