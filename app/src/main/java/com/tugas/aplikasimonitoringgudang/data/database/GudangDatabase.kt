package com.tugas.aplikasimonitoringgudang.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.data.transaksi.TransaksiDao


@Database(entities = [
    Transaksi::class
], version = 1)
abstract class GudangDatabase: RoomDatabase() {

    abstract fun transakasiDao(): TransaksiDao


    companion object {
        @Volatile
        private var INSTANCE: GudangDatabase? = null

        fun getDatabase(context: Context): GudangDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GudangDatabase::class.java,
                    "gudang_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}