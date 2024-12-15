package com.tugas.aplikasimonitoringgudang.api

import com.tugas.aplikasimonitoringgudang.data.supplier.Supplier
import com.tugas.aplikasimonitoringgudang.data.supplier.SupplierResponse
import com.tugas.aplikasimonitoringgudang.data.supplier.SupplierResponseDetail
import retrofit2.http.*

interface ApiSupplierService {
    @GET("/api/supplier")
    suspend fun getSupplier(): SupplierResponse

    @GET("/api/supplier/{id}")
    suspend fun getSupplierById(@Path("id") id: Long): SupplierResponseDetail

    @POST("/api/supplier")
    suspend fun addSupplier(@Body supplier: Supplier): SupplierResponseDetail

    @PUT("/api/supplier/{id}")
    suspend fun updateSupplier(
        @Path("id") id: Long,
        @Body supplier: Supplier
    ): SupplierResponseDetail

    @DELETE("/api/supplier/{id}")
    suspend fun deleteSupplier(@Path("id") id: Long): SupplierResponseDetail
}