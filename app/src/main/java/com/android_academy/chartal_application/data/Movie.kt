package com.android_academy.chartal_application.data


import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val title: String,
    val description: String,
    val review: String,
    val overview: String,
    val age: String,
    var rating: Float,
    val listActors: List<Actor>,
    @DrawableRes val posterRes: Int,
    @DrawableRes val backdropRes: Int,
    val time: String,
    val favorite: Boolean
) : Parcelable