package com.tugas.aplikasimonitoringgudang.data.barang
import androidx.lifecycle.LiveData
import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi

class BarangRepository(private val barangDao: BarangDao) {
    val allBarang: LiveData<List<Barang>> = barangDao.getAllBarang()

    suspend fun insert(barang: Barang) {
        barangDao.insert(barang)
    }

    suspend fun update(barang: Barang){
        barangDao.update(barang)
    }

    suspend fun delete(barang: Barang){
        barangDao.delete(barang)
    }

    suspend fun getBarangById(id: Int): Barang? {
        return barangDao.getBarangById(id)
    }
}