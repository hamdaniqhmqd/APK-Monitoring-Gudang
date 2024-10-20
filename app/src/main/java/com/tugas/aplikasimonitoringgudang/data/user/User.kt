package com.tugas.aplikasimonitoringgudang.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String,
//    val nama: String,
//    val profile: String?,
    val statusLoging: Int = 0,
    )