package com.tugas.aplikasimonitoringgudang.data.barang
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "barang_table",)
data class Barang(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_barang") val id_barang: Int = 0,
    @SerializedName("nama_barang") val nama_barang: String,
    @SerializedName("kategori_barang") val kategori_barang: String,
    @SerializedName("harga_barang") val harga_barang: Int,
    @SerializedName("stok_barang") val stok_barang: Int,
    @SerializedName("ukuran_barang") val ukuran_barang: String,
    @SerializedName("supplier_id") val supplier_id: Int,
    @SerializedName("supplier_nama") val supplier_nama: String,
    @SerializedName("created_at") val created_at: String = "",
    @SerializedName("updated_at") val updated_at: String = ""
)