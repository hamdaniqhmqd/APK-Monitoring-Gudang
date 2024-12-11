package com.tugas.aplikasimonitoringgudang.data.barang

import com.google.gson.annotations.SerializedName

data class BarangResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Barang>
)
