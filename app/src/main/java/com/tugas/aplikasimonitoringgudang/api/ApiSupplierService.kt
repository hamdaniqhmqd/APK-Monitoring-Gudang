package com.tugas.aplikasimonitoringgudang.api

import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.data.supplier.SupplierResponse
import com.tugas.aplikasimonitoringgudang.data.supplier.SupplierResponseDetail
import retrofit2.http.*

// Interface ini mendefinisikan endpoint API untuk mengakses data supplier
interface ApiSupplierService {

    // Fungsi untuk mendapatkan daftar supplier
    @GET("/api/supplier")
    suspend fun getSupplier(): SupplierResponse

    // Fungsi untuk mendapatkan detail supplier berdasarkan ID
    @GET("/api/supplier/{id}")
    suspend fun getSupplierById(@Path("id") id: Long): SupplierResponseDetail

    // Fungsi untuk menambahkan supplier baru
    @POST("/api/supplier")
    // Mengirim data Supplier melalui body request
    suspend fun addSupplier(@Body supplier: Supplier): SupplierResponseDetail

    // Fungsi untuk memperbarui data supplier yang sudah ada berdasarkan ID
    @PUT("/api/supplier/{id}")
    // Menggunakan @Path untuk menggantikan {id} dengan ID yang diminta
    // Menggunakan @Body untuk mengirim data supplier yang sudah diperbarui
    suspend fun updateSupplier(
        @Path("id") id: Long, // ID supplier yang akan diperbarui
        @Body supplier: Supplier // Data supplier yang akan diperbarui
    ): SupplierResponseDetail

    // Fungsi untuk menghapus supplier berdasarkan ID
    @DELETE("/api/supplier/{id}")
    // Menggunakan @Path untuk menggantikan {id} dengan ID yang diminta
    suspend fun deleteSupplier(@Path("id") id: Long): SupplierResponseDetail
}
