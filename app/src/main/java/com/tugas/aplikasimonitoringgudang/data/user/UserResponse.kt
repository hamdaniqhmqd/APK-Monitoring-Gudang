package com.tugas.aplikasimonitoringgudang.data.user

import com.google.gson.annotations.SerializedName
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi

data class UserResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<User>
)
