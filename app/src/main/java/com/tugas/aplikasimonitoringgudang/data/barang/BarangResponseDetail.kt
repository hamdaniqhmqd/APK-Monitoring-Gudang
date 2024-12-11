package com.tugas.aplikasimonitoringgudang.data.barang

import com.google.gson.annotations.SerializedName

data class BarangResponseDetail(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Barang
)
