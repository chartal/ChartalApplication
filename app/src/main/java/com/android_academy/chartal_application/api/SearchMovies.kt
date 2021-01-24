package com.android_academy.chartal_application.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchMovies(
    @SerialName("results")
    val searchMovies: List<SearchMovie>
)

@Serializable
data class SearchMovie(
    @SerialName("id")
    val id: Int
)