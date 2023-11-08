package com.mobatia.nasmanila.api

import com.mobatia.nasmanila.common.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private lateinit var apiService: ApiService

    //    private static final String BASE_URL = "https://beta.mobatia.in:8888/nas-dubai-development/";
    //    private static final String BASE_URL = "https://beta.mobatia.in:8888/nais-dubai-penetration-fix/";
    private const val BASE_URL = "http://gama.mobatia.in:8080/NAIS-Manila2023/public/"
    private val retrofit: Retrofit? = null

    //    private static final String BASE_URL = "https://cms.nasdubai.ae/nais/";
    fun getApiService(): ApiService {
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(" http://manila.mobatia.in:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        apiService = retrofit.create(ApiService::class.java)
        return apiService
    }
}