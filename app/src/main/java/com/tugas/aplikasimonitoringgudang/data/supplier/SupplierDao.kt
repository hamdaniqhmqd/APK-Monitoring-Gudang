package com.tugas.aplikasimonitoringgudang.data.supplier

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SupplierDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg supplier: Supplier)

    @Update
    suspend fun update(vararg supplier: Supplier)

    @Delete
    suspend fun delete(vararg supplier: Supplier)

    @Query("SELECT * FROM supplier_table ORDER BY id_supplier ASC")
    fun getAllSupplier(): LiveData<List<Supplier>>

    @Query("SELECT * FROM supplier_table WHERE id_supplier = :id")
    suspend fun getSupplierById(id: Int): Supplier?
}