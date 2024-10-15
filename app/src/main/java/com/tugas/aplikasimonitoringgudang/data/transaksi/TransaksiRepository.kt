package com.tugas.aplikasimonitoringgudang.data.transaksi

import androidx.lifecycle.LiveData

class TransaksiRepository(private val transaksiDao: TransaksiDao) {

    val allCategories: LiveData<List<Transaksi>> = transaksiDao.getAllTransaksi()

    suspend fun insert(category: Transaksi) {
        transaksiDao.insert(category)
    }

    suspend fun update(category: Transaksi) {
        transaksiDao.update(category)
    }

    suspend fun delete(category: Transaksi) {
        transaksiDao.delete(category)
    }

}