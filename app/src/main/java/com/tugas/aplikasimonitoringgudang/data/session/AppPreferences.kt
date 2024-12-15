package com.tugas.aplikasimonitoringgudang.data.session

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val PREFS_NAME = "AppPrefs" // Nama file SharedPreferences
    private const val KEY_USER_ID = "id_user"
    private const val KEY_USERNAME = "username"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"

    private lateinit var preferences: SharedPreferences

    // Inisialisasi SharedPreferences, biasanya di Application class
    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Menyimpan User ID
    fun setUserId(userId: Long) {
        preferences.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Long {
        return preferences.getLong(KEY_USER_ID, -1) // Default -1 jika tidak ada
    }

    // Menyimpan Username
    fun setUsername(username: String) {
        preferences.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(): String? {
        return preferences.getString(KEY_USERNAME, null) // Default null jika tidak ada
    }

    // Menyimpan Status Login
    fun setLoggedIn(isLoggedIn: Boolean) {
        preferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false) // Default false jika tidak ada
    }

    // Membersihkan Semua Data (Misalnya saat logout)
    fun clear() {
        preferences.edit().clear().apply()
    }
}