package com.android_academy.chartal_application.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "films")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val overview: String,
    val poster: String,
    val backdrop: String,
    val ratings: Float,
    val numberOfRatings: Int,
    val minimumAge: Int,
    val runtime: Int,
    @Ignore
    val genres: List<Genre>?,
    @Ignore
    var actors: List<Actor>?
) : Parcelable {
    constructor(
        id: Int,
        title: String,
        overview: String,
        poster: String,
        backdrop: String,
        ratings: Float,
        numberOfRatings: Int,
        minimumAge: Int,
        runtime: Int
    ) : this(
        id,
        title,
        overview,
        poster,
        backdrop,
        ratings,
        numberOfRatings,
        minimumAge,
        runtime,
        null,
        null
    )
}