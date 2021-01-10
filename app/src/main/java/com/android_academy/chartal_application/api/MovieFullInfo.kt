package com.android_academy.chartal_application.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieFullInfo(

    @SerialName("adult")
    val adult: Boolean,

    @SerialName("id")
    val id: Int,

    @SerialName("runtime")
    val runtime: Int,

    @SerialName("title")
    val title: String,

    @SerialName("backdrop_path")
    val backdrop_path: String,

    @SerialName("poster_path")
    val poster_path: String,

    @SerialName("vote_average")
    val vote_average: Double,

    @SerialName("vote_count")
    val voteCount: Int,

    @SerialName("video")
    val video: Boolean,

    @SerialName("overview")
    val overview: String,

    @SerialName("genres")
    val genres: List<Genre1>
)

@Parcelize
@Serializable
data class Genre1(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
) : Parcelable