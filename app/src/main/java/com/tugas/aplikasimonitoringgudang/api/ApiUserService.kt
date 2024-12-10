package com.tugas.aplikasimonitoringgudang.api

import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.data.user.UserResponse
import com.tugas.aplikasimonitoringgudang.data.user.UserResponseDetail
import retrofit2.http.*

interface ApiUserService {
    @GET("/api/admin")
    suspend fun getAllAdmins(): UserResponse

    @GET("/api/admin/{id}")
    suspend fun getAdminById(@Path("id") id: Int): UserResponseDetail

    @POST("/api/admin")
    suspend fun createAdmin(@Body user: User): UserResponseDetail

    @PUT("/api/admin/{id}")
    suspend fun updateAdmin(@Path("id") id: Int, @Body user: User): UserResponseDetail

    @DELETE("/api/admin/{id}")
    suspend fun deleteAdmin(@Path("id") id: Int): UserResponseDetail

    @POST("/api/login")
    suspend fun loginAdmin(@Body user: User): UserResponseDetail
}
