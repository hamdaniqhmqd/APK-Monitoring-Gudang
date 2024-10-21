package com.tugas.aplikasimonitoringgudang.data.transaksi

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

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

    @Query("SELECT * FROM transaksi_table WHERE id_transaksi = :id")
    fun getTransaksiById(id: Int): LiveData<Transaksi>

    @Query("SELECT COUNT(*) FROM transaksi_table WHERE status = 2")
    suspend fun getTransaksiMasukCount(): Int

    @Query("SELECT COUNT(*) FROM transaksi_table WHERE status = 1")
    suspend fun getTransaksiKeluarCount(): Int

    @Query("SELECT COUNT(DISTINCT barang_nama) FROM transaksi_table WHERE status = 2")
    fun getUniqueBarangCountInTransaksiMasuk(): LiveData<Int>
}
