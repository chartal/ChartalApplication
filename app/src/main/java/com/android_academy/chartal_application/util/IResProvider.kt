package com.android_academy.chartal_application.util

import com.android_academy.chartal_application.data.Movie

interface IResProvider {
   suspend fun loadFilms():List<Movie>
}