package com.tugas.aplikasimonitoringgudang.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.data.user.UserDao
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.data.transaksi.TransaksiDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Transaksi::class], version = 1)
abstract class GudangDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
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
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    getDatabase(context).userDao().insert(User(username = "admin", password = "admin"))
                                    Log.d("GudangDatabase", "Admin user inserted successfully")
                                } catch (e: Exception) {
                                    Log.e("GudangDatabase", "Error inserting admin user", e)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}