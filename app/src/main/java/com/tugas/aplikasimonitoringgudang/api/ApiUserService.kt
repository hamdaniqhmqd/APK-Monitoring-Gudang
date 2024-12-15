package com.tugas.aplikasimonitoringgudang.api

import com.tugas.aplikasimonitoringgudang.data.user.User
import com.tugas.aplikasimonitoringgudang.data.user.UserResponse
import com.tugas.aplikasimonitoringgudang.data.user.UserResponseDetail
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiUserService {
    @GET("/api/admin")
    suspend fun getAllAdmins(): UserResponse

    @GET("/api/admin/{id}")
    suspend fun getAdminById(@Path("id") id: Long): UserResponseDetail

    @POST("/api/admin")
    suspend fun createAdmin(
        @Body user: User
    ): UserResponseDetail

    @Multipart
    @POST("/api/admin/{id}")
    suspend fun updateAdmin(
        @Path("id") id: Long,
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
        @Part("adminName") adminName: RequestBody,
        @Part profileImagePath: MultipartBody.Part?, // Tanpa nama bagian
        @Part("_method") method: RequestBody
    ): UserResponseDetail

    @DELETE("/api/admin/{id}")
    suspend fun deleteAdmin(@Path("id") id: Long): UserResponseDetail

    @POST("/api/login")
    suspend fun loginAdmin(@Body user: User): UserResponseDetail
}

