package com.tugas.aplikasimonitoringgudang.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("adminName") val adminName: String,
    @SerializedName("profileImagePath") val profileImagePath: String? = null,
)