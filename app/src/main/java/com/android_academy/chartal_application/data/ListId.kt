package com.android_academy.chartal_application.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ids")
data class ListId(
    @PrimaryKey(autoGenerate = true)
    val movieId: Int,
    @Ignore
    var actors: List<ActorDb>?
) : Parcelable {
    constructor(
        movieId: Int
    ) : this(
        movieId,
        null
    )
}