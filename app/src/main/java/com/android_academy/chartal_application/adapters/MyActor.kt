package com.android_academy.chartal_application.adapters

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize


@Parcelize
data class MyActor(
    @DrawableRes val image: Int,
    val name: String
) : Parcelable