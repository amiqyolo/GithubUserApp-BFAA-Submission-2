package com.android.githubuserapp.networking

import com.android.githubuserapp.utility.Constants.API_TOKEN
import com.android.githubuserapp.utility.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().apply {
                addInterceptor { chain ->
                    val request: Request =
                        chain.request().newBuilder().apply {
                            addHeader("Authorization", "token $API_TOKEN")
                        }.build()
                    chain.proceed(request)
                }
                addInterceptor(loggingInterceptor)
            }.build()

            val retrofit = Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                client(client)
            }.build()

            return retrofit.create(ApiService::class.java)
        }
    }
}