package com.example.tagfinderapp.Network

import com.example.tagfinderapp.Inteface.ApiInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstanse {

    private const val BASE_URL = "https://www.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
        .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
        .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
        .build()

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
    fun getKeywordsRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://suggestqueries.google.com/complete/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}