package com.android_academy.chartal_application.util

import android.content.Context
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.data.loadMovies

class ResProvider(val context: Context) : IResProvider {

    override suspend fun loadFilms(): List<Movie> {
        return loadMovies(context)
    }

}