package com.tugas.aplikasimonitoringgudang.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.barang.BarangDao
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.data.user.UserDao
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.data.transaksi.TransaksiDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Transaksi::class, Barang::class], version = 1)
abstract class GudangDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun transakasiDao(): TransaksiDao
    abstract fun barangDao(): BarangDao


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