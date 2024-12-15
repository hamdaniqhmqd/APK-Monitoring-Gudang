package com.tugas.aplikasimonitoringgudang.data.transaksi

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "transaksi_table")
data class Transaksi(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id_transaksi") val id_transaksi: Long = 0,
    @SerializedName("barang_id") val barang_id: Long = 0,
    @SerializedName("jumlah_barang") val jumlah_barang: Int,
    @SerializedName("total_harga_barang") val total_harga_barang: Int,
    @SerializedName("user_id") val user_id: Long,
    @SerializedName("supplier_id") val supplier_id: Long,
    @SerializedName("bulan") val bulan: String,
    @SerializedName("tanggal") val tanggal: String,
    @SerializedName("tanggalAkhir") val tanggalAkhir: String = "",
    @SerializedName("status") val status: Int,
    @SerializedName("statusAkhir") val statusAkhir: Int = 0,
    @SerializedName("created_at") val created_at: String = "",
    @SerializedName("updated_at") val updated_at: String = ""
)
