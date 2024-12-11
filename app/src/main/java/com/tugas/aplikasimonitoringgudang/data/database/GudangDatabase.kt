package com.tugas.aplikasimonitoringgudang.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.local.BarangDao
import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.local.SupplierDao
import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.local.UserDao
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.local.TransaksiDao

@Database(entities = [User::class, Transaksi::class, Barang::class, Supplier::class], version = 1)
abstract class GudangDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun transaksiDao(): TransaksiDao
    abstract fun barangDao(): BarangDao
    abstract fun supplierDao(): SupplierDao

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
