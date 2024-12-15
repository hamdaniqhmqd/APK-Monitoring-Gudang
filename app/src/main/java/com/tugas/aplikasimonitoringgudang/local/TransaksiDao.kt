package com.tugas.aplikasimonitoringgudang.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi

@Dao
interface TransaksiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg transaksi: Transaksi)

    @Update
    suspend fun update(vararg transaksi: Transaksi)

    @Delete
    suspend fun delete(vararg transaksi: Transaksi)

    @Query("SELECT * FROM transaksi_table ORDER BY id_transaksi ASC")
    suspend fun getAllTransaksi(): List<Transaksi>

    @Query("SELECT * FROM transaksi_table WHERE id_transaksi = :id")
    suspend fun getTransaksiById(id: Long): Transaksi

    //

    @Query("SELECT COUNT(*) FROM transaksi_table WHERE status = 2")
    suspend fun getTransaksiMasukCount(): Int

    @Query("SELECT COUNT(*) FROM transaksi_table WHERE status = 1")
    suspend fun getTransaksiKeluarCount(): Int
}
