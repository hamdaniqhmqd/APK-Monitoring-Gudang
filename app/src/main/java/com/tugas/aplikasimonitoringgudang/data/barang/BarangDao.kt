package com.tugas.aplikasimonitoringgudang.data.barang

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BarangDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg barang: Barang)

    @Update
    suspend fun update(vararg barang: Barang)

    @Delete
    suspend fun delete(vararg barang: Barang)

    @Query("SELECT * FROM barang_table ORDER BY id_barang ASC")
    fun getAllBarang(): LiveData<List<Barang>>

    @Query("SELECT * FROM barang_table WHERE id_barang = :id")
    fun getBarangById(id: Int): LiveData<Barang>
}