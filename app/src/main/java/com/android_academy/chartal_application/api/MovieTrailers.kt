package com.android_academy.chartal_application.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieTrailers(
    @SerialName("id")
    val id: Int,
    @SerialName("results")
    val results: List<Trailer>
)

@Serializable
data class Trailer(
    @SerialName("key")
    val key: String
)