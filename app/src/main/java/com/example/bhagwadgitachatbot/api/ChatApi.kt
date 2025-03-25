package com.example.bhagwadgitachatbot.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("query")
    suspend fun getChatResponse(
        @Body request: ChatRequest
    ): Response<ChatResponse>
} 