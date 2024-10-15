package com.tugas.aplikasimonitoringgudang.data.transaksi

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransaksiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(barang: Transaksi)

    @Update
    suspend fun update(barang: Transaksi)

    @Delete
    suspend fun delete(barang: Transaksi)

    @Query("SELECT * FROM transaksi_table ORDER BY id_transaksi ASC")
    fun getAllTransaksi(): LiveData<List<Transaksi>>


}
