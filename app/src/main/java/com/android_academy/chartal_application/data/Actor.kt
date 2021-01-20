package com.android_academy.chartal_application.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Actor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val picture: String
): Parcelable