package com.android_academy.chartal_application.data

import android.os.Parcelable
import com.android_academy.chartal_application.api.Genre1
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    val ratings: Float,
    val numberOfRatings: Int,
    val minimumAge: Int,
    val runtime: Int,
    val genres: List<Genre>,
    var actors: List<Actor>
): Parcelable