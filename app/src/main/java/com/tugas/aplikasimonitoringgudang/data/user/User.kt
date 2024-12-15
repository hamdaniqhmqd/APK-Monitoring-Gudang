package com.tugas.aplikasimonitoringgudang.data.user

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table",
    indices = [Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id") val id: Long = 0,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("adminName") val adminName: String,
    @SerializedName("profileImagePath") val profileImagePath: String? = null,
    @SerializedName("created_at") val created_at: String = "",
    @SerializedName("updated_at") val updated_at: String = ""
)
