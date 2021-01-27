package com.android_academy.chartal_application.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "userfilms")
data class UserMovie(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String?
) : Parcelable