package com.tugas.aplikasimonitoringgudang.data.transaksi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi_table", )
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    val id_transaksi: Int = 0,
    val barang_id: Int = 0,
    val barang_nama: String,
    val kategori_barang: String,
    val harga_barang: Int,
    val stok_barang: Int,
    val ukuran_barang: String,
    val jumlah_barang: Int,
    val total_harga_barang: Int,
    val user_id: Int,
    val user_nama: String,
    val supplier_id: Int,
    val supplier_nama: String,
    val bulan: String,
    val tanggal: String,
    val tanggalAkhir: String = "",
    val status: Int,
    val statusAkhir: Int = 0,
)
