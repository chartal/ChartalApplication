package com.android_academy.chartal_application.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Actor(
    @DrawableRes val image: Int,
    val name: String
): Parcelable
