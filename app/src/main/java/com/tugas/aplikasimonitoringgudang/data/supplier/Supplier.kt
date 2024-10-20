package com.tugas.aplikasimonitoringgudang.data.supplier

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier_table")
data class Supplier(
    @PrimaryKey(autoGenerate = true)
    val id_supplier: Int = 0,
    val nama_supplier: String,
    val nik_supplier: Int,
    val no_hp_supplier: Int,
) {

}
