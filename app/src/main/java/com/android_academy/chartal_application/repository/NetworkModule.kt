package com.android_academy.chartal_application.repository

import com.android_academy.chartal_application.api.TheMovieDb
import com.android_academy.chartal_application.util.DataConverter
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

private const val BASE_URL = "https://api.themoviedb.org/3/"

object NetworkModule {

    val filmsRepository by lazy {
        FilmsRepository(createTmdbServiceApi(), DataConverter())
    }

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private fun createTmdbServiceApi(): TheMovieDb {
        return createRetrofit().create(TheMovieDb::class.java)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl((BASE_URL))
            .build()
    }
}