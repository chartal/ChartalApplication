package com.android_academy.chartal_application.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val id: Int,
    val name: String
) : Parcelable {
    override fun toString(): String {
        return name
    }
}