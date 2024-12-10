package com.tugas.aplikasimonitoringgudang.data.user

import com.google.gson.annotations.SerializedName

data class UserResponseDetail(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: User
)
