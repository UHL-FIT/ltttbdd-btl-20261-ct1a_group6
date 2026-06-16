package com.example.nhatky

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class QuoteResponse(
    val q: String, // Câu châm ngôn
    val a: String  // Tác giả
)

interface ZenQuotesService {
    @GET("api/quote")
    suspend fun getDailyQuote(): List<QuoteResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://mock.apidog.com/m1/1311314-1311193-default/"

    val instance: ZenQuotesService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZenQuotesService::class.java)
    }
}