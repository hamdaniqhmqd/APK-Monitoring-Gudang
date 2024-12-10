package com.tugas.aplikasimonitoringgudang.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceGudangApi {
    private const val BASE_URL = "https://gudang-pakaian-api.infitechd.my.id/"
//    private const val BASE_URL = "http://127.0.0.1:8000/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // variabel untuk endpoint admin

    // variabel untuk endpoint supplier

    // variabel untuk endpoint barang

    // variabel untuk endpoint transaksi
    val apiTransaksiService: ApiTransaksiService by lazy {
        retrofit.create(ApiTransaksiService::class.java)
    }
}