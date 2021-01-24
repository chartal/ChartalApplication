package com.android_academy.chartal_application.api

import com.android_academy.chartal_application.repository.apiKey
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDb {

    @GET("movie/{movie_id}?")
    suspend fun getMovieFullInfo(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = apiKey
    ): MovieFullInfo

    @GET("movie/popular?")
    suspend fun getListId(
        @Query("page") page: Int,
        @Query("api_key") api_key: String = apiKey
    ): Response

    @GET("movie/{movie_id}/credits?")
    suspend fun getActors(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = apiKey
    ): MovieCast

    @GET("movie/{movie_id}/videos?")
    suspend fun getMovieVideos(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = apiKey
    ): MovieTrailers

    @GET("search/movie?")
    suspend fun getSearchMovieListId(
        @Query("query") query: String,
        @Query("api_key") api_key: String = apiKey
    ): SearchMovies
}