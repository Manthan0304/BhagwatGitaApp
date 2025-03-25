package com.example.bhagwadgitachatbot.api

data class ChatRequest(
    val query: String,
    val num_verses: Int = 3
)

data class Verse(
    val verse_number: String,
    val translation_in_english: String
)

data class ChatResponse(
    val response: String,
    val relevant_verses: List<Verse>
) 