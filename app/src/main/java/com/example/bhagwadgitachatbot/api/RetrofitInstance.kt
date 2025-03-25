package com.example.bhagwadgitachatbot.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "YOUR_NGROK_URL/" // Replace with your ngrok URL

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ChatApi by lazy {
        retrofit.create(ChatApi::class.java)
    }
} 