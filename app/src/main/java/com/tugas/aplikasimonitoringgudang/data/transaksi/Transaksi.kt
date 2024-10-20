package com.tugas.aplikasimonitoringgudang.data.transaksi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi_table", )
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    val id_transaksi: Int = 0,
//    val barang_id: Int,
    val barang_nama: String,
//    val user_id: Int,
//    val user_nama: String,
//    val supplier_id: Int,
//    val supplier_nama: String,
    val jumlah_barang: Int,
    val harga_barang: Int,
    val total_harga_barang: Int,
//    val tanggal: String,
    val status: Int,
)
