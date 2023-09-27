package com.rival.githubusersapp.data.network
import com.rival.githubusersapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    val token = BuildConfig.API_KEY
    val authInterceptor = Interceptor { chain ->
        val req = chain.request()
        val requestHeaders = req.newBuilder()
            .addHeader("Authorization", "$token")
            .build()
        chain.proceed(requestHeaders)
    }

    fun getApiService(): ApiService {
        val apiUrl = BuildConfig.API_URL
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("$apiUrl")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }


}