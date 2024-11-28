package com.example.submission1intermediate.retrofit

import ApiService
import android.content.Context
import android.util.Log
import com.example.submission1intermediate.MyApplication
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    // Interceptor untuk logging request/response
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor untuk menambahkan header Authorization (token)
    private val authInterceptor = Interceptor { chain ->
        val token = getTokenFromSharedPreferences()
        if (token.isEmpty()) {
            Log.e("RetrofitClient", "Token kosong!")
        }
        val request: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }

    // Fungsi untuk mengambil token dari SharedPreferences
    private fun getTokenFromSharedPreferences(): String {
        val context = MyApplication.instance.applicationContext
        val sharedPref = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val token = sharedPref.getString("authToken", "")
        return token ?: ""
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(ApiService::class.java)
}
