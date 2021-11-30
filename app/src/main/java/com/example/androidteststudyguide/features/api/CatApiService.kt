package com.example.androidteststudyguide.features.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface CatApiService {


    @GET("breeds/")
    suspend fun getCatImages(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): List<CatBreedResponse>

    companion object {
        private const val BASE_URL = "https://api.thecatapi.com/v1/"

        fun create(): CatApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()

                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS) // this line fix tiem out problem
                    // add header to every request
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("x-api-key", "f81b24bd-e6a1-417a-9b5e-da7f3cbc5c4d")
                        .build()
                    return@Interceptor chain.proceed(request)
                })
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CatApiService::class.java)
        }
    }
}