package com.android_academy.chartal_application.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDb {

    @GET("movie/{movie_id}?api_key=e21bbf9fde1ed4eb09d04d9215a495df&language=en-US")
    suspend fun getMovieFullInfo(@Path("movie_id") movie_id: Int): MovieFullInfo

    @GET("movie/popular?api_key=e21bbf9fde1ed4eb09d04d9215a495df&language=en-US&page=1")
    suspend fun getListId(@Query("page") page: Int): Response

    @GET("movie/{movie_id}/credits?api_key=e21bbf9fde1ed4eb09d04d9215a495df&language=en-US")
    suspend fun getActors(@Path("movie_id") movie_id: Int): MovieCast

    @GET("movie/{movie_id}/videos?api_key=e21bbf9fde1ed4eb09d04d9215a495df&language=en-US")
    suspend fun getMovieVideos(
        @Path("movie_id") movie_id: Int,
    ): MovieTrailers

    @GET("search/movie?api_key=e21bbf9fde1ed4eb09d04d9215a495df")
    suspend fun getSearchMovieListId(
        @Query("query") query: String
    ): SearchMovies
}