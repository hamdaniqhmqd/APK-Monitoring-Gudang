package com.tugas.aplikasimonitoringgudang.api

import com.tugas.aplikasimonitoringgudang.data.transaksi.Transaksi
import com.tugas.aplikasimonitoringgudang.data.transaksi.TransaksiResponse
import com.tugas.aplikasimonitoringgudang.data.transaksi.TransaksiResponseDetail
import retrofit2.http.*

interface ApiTransaksiService {
    @GET("/api/transaksi")
    suspend fun getTransaksi(): TransaksiResponse

    @GET("/api/transaksi/{id}")
    suspend fun getTransaksiById(@Path("id") id: Long): TransaksiResponseDetail

    @POST("/api/transaksi")
    suspend fun addTransaksi(@Body transaksi: Transaksi): TransaksiResponseDetail

    @PUT("/api/transaksi/{id}")
    suspend fun updateTransaksi(
        @Path("id") id: Long,
        @Body transaksi: Transaksi
    ): TransaksiResponseDetail

    @DELETE("/api/transaksi/{id}")
    suspend fun deleteTransaksi(@Path("id") id: Long): TransaksiResponseDetail
}