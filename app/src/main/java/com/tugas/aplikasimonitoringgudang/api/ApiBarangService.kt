package com.tugas.aplikasimonitoringgudang.api

import com.tugas.aplikasimonitoringgudang.data.barang.Barang
import com.tugas.aplikasimonitoringgudang.data.barang.BarangResponse
import com.tugas.aplikasimonitoringgudang.data.barang.BarangResponseDetail
import retrofit2.http.*

interface ApiBarangService {
    @GET("/api/barang")
    suspend fun getBarang(): BarangResponse

    @GET("/api/barang/{id}")
    suspend fun getBarangById(@Path("id") id: Int): BarangResponseDetail

    @POST("/api/barang")
    suspend fun addBarang(@Body barang: Barang): BarangResponseDetail

    @PUT("/api/barang/{id}")
    suspend fun updateBarang(
        @Path("id") id: Int,
        @Body barang: Barang
    ): BarangResponseDetail

    @DELETE("/api/barang/{id}")
    suspend fun deleteBarang(@Path("id") id: Int): BarangResponseDetail

    @GET("/barang/supplier/{id}")
    suspend fun getBarangByIdSupplier(@Path("id") id: Int): BarangResponse
}