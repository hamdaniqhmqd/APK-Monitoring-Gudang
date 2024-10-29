package com.tugas.aplikasimonitoringgudang.data.barang

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Daftar entitas yang digunakan di dalam database dan versi database
@Database(entities = [Barang::class], version = 1, exportSchema = false)
abstract class BarangDatabase : RoomDatabase() {
    // DAO yang berfungsi untuk mengakses data dari database
    abstract fun barangDao(): BarangDao

    companion object {
        // Tambahkan singleton instance untuk mencegah beberapa instance database terbuka secara bersamaan
        @Volatile
        private var INSTANCE: BarangDatabase? = null

        // Method untuk mendapatkan instance dari database
        fun getDatabase(context: Context): BarangDatabase {
            // Jika instance sudah ada, kembalikan instance tersebut
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // Jika belum ada, buat instance baru
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BarangDatabase::class.java,
                    "barang_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
