package com.android_academy.chartal_application.details

import com.android_academy.chartal_application.data.Movie

interface TransactionsFragmentClicks {
    fun addFragmentMoviesDetails(movie: Movie)
    fun addFragmentMoviesList()
}