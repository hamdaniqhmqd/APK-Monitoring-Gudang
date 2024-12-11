package com.tugas.aplikasimonitoringgudang.data.supplier

import com.google.gson.annotations.SerializedName

data class SupplierResponseDetail(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Supplier
)
