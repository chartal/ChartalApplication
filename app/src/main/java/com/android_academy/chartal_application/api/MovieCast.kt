package com.android_academy.chartal_application.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCast(
    val id: Long,
    val cast: List<Cast>
)

@Serializable
data class Cast(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    val profilePath: String? = null
)