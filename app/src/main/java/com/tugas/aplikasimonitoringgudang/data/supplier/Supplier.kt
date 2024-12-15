package com.tugas.aplikasimonitoringgudang.data.supplier

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "supplier_table")
data class Supplier(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id_supplier") val id_supplier: Long = 0,
    @SerializedName("nama_supplier") val nama_supplier: String,
    @SerializedName("nik_supplier") val nik_supplier: String,
    @SerializedName("no_hp_supplier") val no_hp_supplier: String,
    @SerializedName("created_at") val created_at: String = "",
    @SerializedName("updated_at") val updated_at: String = ""
)
