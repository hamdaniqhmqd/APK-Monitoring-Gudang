package com.tugas.aplikasimonitoringgudang.data.barang
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barang_table",)
data class Barang(
    @PrimaryKey(autoGenerate = true)
    val id_barang: Int = 0,
    val nama_barang: String,
    val kategori_barang: String,
    val harga_barang: Int,
    val stok_barang: Int,
    val ukuran_barang: String,
    val supplier_id: Int,
    val supplier_nama: String,
)