package com.tugas.aplikasimonitoringgudang.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tugas.aplikasimonitoringgudang.data.barang.Barang

@Dao
interface BarangDao {
    @Insert
    suspend fun insert(barang: Barang): Long

    @Update
    suspend fun update(vararg barang: Barang)

    @Delete
    suspend fun delete(vararg barang: Barang)

    @Query("SELECT * FROM barang_table ORDER BY id_barang ASC")
    fun getAllBarangGudang(): List<Barang>

    @Query("SELECT * FROM barang_table ORDER BY id_barang ASC")
    fun getAllBarang(): List<Barang>

    @Query("SELECT * FROM barang_table WHERE id_barang = :id")
    fun getBarangById(id: Int): Barang

    @Query("SELECT * FROM barang_table WHERE supplier_id = :supplierId ORDER BY supplier_id ASC")
    fun getBarangByIdSuppler(supplierId: Int): List<Barang>

//    @Query("SELECT * FROM barang_table WHERE ")
}
