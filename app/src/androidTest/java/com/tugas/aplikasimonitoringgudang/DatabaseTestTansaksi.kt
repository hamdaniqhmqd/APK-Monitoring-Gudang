package com.tugas.aplikasimonitoringgudang

//@RunWith(AndroidJUnit4::class)
//class DatabaseTestTansaksi {
//    private lateinit var database: GudangDatabase
//    private lateinit var transaksi: TransaksiDao
//
//    val satu = Transaksi(
//        id_transaksi = 1, barang_nama = "Kaos", supplier_nama = "Ahmad", user_nama = "amat",
//        harga_barang = 1000, jumlah_barang = 10, total_harga_barang = 10000, status = 1
//    )
//
//    val dua = Transaksi(
//        id_transaksi = 2, barang_nama = "Baju", supplier_nama = "Ahmad", user_nama = "amat",
//        harga_barang = 1000, jumlah_barang = 10, total_harga_barang = 10000, status = 1
//    )
//
//    val tiga = Transaksi(
//        id_transaksi = 3, barang_nama = "Celana", supplier_nama = "Ahmad", user_nama = "amat",
//        harga_barang = 1000, jumlah_barang = 10, total_harga_barang = 10000, status = 1
//    )
//
//    @Before
//    fun setUp() {
//        val context: Context = ApplicationProvider.getApplicationContext()
//        database = Room.inMemoryDatabaseBuilder(
//            context,
//            GudangDatabase::class.java
//        ).build()
//        transaksi = database.transakasiDao()
//    }
//
//    @After
//    @Throws(IOException::class)
//    fun dbClose() = database.close()
//
//    @Test
//    @Throws(Exception::class)
//    fun insertAndRetrieve() {
//        transaksi.insert(satu, dua, tiga)
//        val dataTransaksi = transaksi.getAllItemTransaksi()
//        assert(dataTransaksi.size == 3)
//    }
//
//    val baru = Transaksi(
//        id_transaksi = 2,  barang_nama = "Celana", supplier_nama = "Ahmad", user_nama = "amat",
//        harga_barang = 1000, jumlah_barang = 10, total_harga_barang = 10000, status = 1
//    )
//
//    @Test
//    @Throws(Exception::class)
//    fun updateAndRetrieve() {
//        transaksi.insert(dua)
//        transaksi.update(baru)
//        val dataTransaksi = transaksi.getAllItemTransaksi()
//        assert(dataTransaksi.size == 1)
//    }
//
//    val hapus = Transaksi(
//        id_transaksi = 3,  barang_nama = "", supplier_nama = "", user_nama = "",
//        harga_barang = 0, jumlah_barang = 0, total_harga_barang = 0, status = 0
//    )
//
//    @Test
//    @Throws(Exception::class)
//    fun deleteAndRetrieve() {
//        transaksi.insert(tiga)
//        transaksi.delete(hapus)
//    }
//}