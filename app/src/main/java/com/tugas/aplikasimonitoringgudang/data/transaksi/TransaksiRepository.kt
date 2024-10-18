package com.tugas.aplikasimonitoringgudang.data.transaksi

import androidx.lifecycle.LiveData

class TransaksiRepository(private val transaksiDao: TransaksiDao) {

    val allTransaksi: LiveData<List<Transaksi>> = transaksiDao.getAllTransaksi()

    suspend fun insert(transaksi: Transaksi) {
        transaksiDao.insert(transaksi)
    }

    suspend fun update(transaksi: Transaksi) {
        transaksiDao.update(transaksi)
    }

    suspend fun delete(transaksi: Transaksi) {
        transaksiDao.delete(transaksi)
    }

    suspend fun getTransaksiById(id: Int): Transaksi? {
        return transaksiDao.getBarangById(id)
    }
}