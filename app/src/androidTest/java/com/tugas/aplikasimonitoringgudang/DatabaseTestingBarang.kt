//package com.tugas.aplikasimonitoringgudang
//
//import android.content.Context
//import androidx.room.Room
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.tugas.aplikasimonitoringgudang.data.barang.Barang
//import com.tugas.aplikasimonitoringgudang.local.BarangDao
//import com.tugas.aplikasimonitoringgudang.data.barang.BarangDatabase
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import java.io.IOException
//import kotlin.jvm.Throws
//
//@RunWith(AndroidJUnit4::class)
//class DatabaseTestingBarang {
//
//    private lateinit var barangDao: BarangDao
//    private lateinit var db: BarangDatabase
//
//    val bajuBatik = Barang(
//        id_barang = 1,
//        nama_barang = "Baju Batik",
//        kategori_barang = "Dewasa",
//        harga_barang = 50000,
//        stok_barang = 5,
//        ukuran_barang = "M",
//        supplier_id = 1,
//        supplier_nama = "Anis"
//    )
//    val bajuLebaran = Barang(
//        id_barang = 2,
//        nama_barang = "Baju Koko",
//        kategori_barang = "Dewasa",
//        harga_barang = 500000,
//        stok_barang = 10,
//        ukuran_barang = "L",
//        supplier_id = 1,
//        supplier_nama = "Anis"
//    )
//
//    val bajuAnak = Barang(
//        id_barang = 2,
//        nama_barang = "Baju Koko",
//        kategori_barang = "Anak",
//        harga_barang = 55000,
//        stok_barang = 10,
//        ukuran_barang = "S",
//        supplier_id = 1,
//        supplier_nama = "Anis"
//    )
//
//    @Before
//    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        db = Room.inMemoryDatabaseBuilder(context, BarangDatabase::class.java)
//            .allowMainThreadQueries() //Hanya untuk testing, jangan digunakan di produksi
//            .build()
//
//        barangDao = db.barangDao() //inisialisasi Dao
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun closeDb() = db.close()
//
//    @Test
//    @Throws(Exception::class)
//    fun insertAndRetrieve() {
//        barangDao.insert(bajuBatik, bajuLebaran, bajuAnak)
//        val barang = barangDao.getAllBarangGudang()
//        println("ISIAN BARANG = ${barang}")
////        assert(barang.size==3)
//    }
//}