package com.android_academy.chartal_application.adapters


import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize


@Parcelize
data class MyMovie(
    val title: String,
    val description: String,
    val review: String,
    val overview: String,
    val age: String,
    var rating: Float,
    val listMyActors: List<MyActor>,
    @DrawableRes val posterRes: Int,
    @DrawableRes val backdropRes: Int,
    val time: String,
    val favorite: Boolean
) : Parcelable