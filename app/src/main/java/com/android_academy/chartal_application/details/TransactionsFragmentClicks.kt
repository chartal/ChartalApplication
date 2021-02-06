package com.android_academy.chartal_application.details


import com.android_academy.chartal_application.data.Movie

interface TransactionsFragmentClicks {
    fun addGalleryFragment(movie: Movie, movies: List<Movie>, position: Int, flag: Boolean)
    fun addFragmentMoviesList()
}