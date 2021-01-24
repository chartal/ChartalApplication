package com.android_academy.chartal_application.util

import com.android_academy.chartal_application.api.MovieFullInfo
import com.android_academy.chartal_application.data.Genre
import com.android_academy.chartal_application.data.Movie


class DataConverter {

    fun convertToMovie(list: List<MovieFullInfo>): List<Movie> {
        return list.map { it -> convertToMovie(it) }
    }

    private fun convertToMovie(movieFullInfo: MovieFullInfo): Movie {
        return Movie(
            id = movieFullInfo.id,
            title = movieFullInfo.title,
            overview = movieFullInfo.overview,
            poster = BASE_POSTER_URL + movieFullInfo.poster_path,
            backdrop = SECURE_BASE_URL + movieFullInfo.backdrop_path,
            ratings = (movieFullInfo.vote_average / 2).toFloat(),
            numberOfRatings = movieFullInfo.voteCount,
            minimumAge = if (movieFullInfo.adult) 16 else 13,
            runtime = movieFullInfo.runtime,
            genres = movieFullInfo.genres.map { Genre(it.id, it.name) },
            actors = emptyList()
        )
    }

    companion object {
        private const val BASE_POSTER_URL = "http://image.tmdb.org/t/p/w300"
        private const val SECURE_BASE_URL = "https://image.tmdb.org/t/p/w300"
    }
}