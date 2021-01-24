package com.android_academy.chartal_application.repository

import com.android_academy.chartal_application.api.MovieFullInfo
import com.android_academy.chartal_application.api.TheMovieDb
import com.android_academy.chartal_application.data.Actor
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.room.AppDatabase
import com.android_academy.chartal_application.room.UserDatabase
import com.android_academy.chartal_application.util.DataConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilmsRepository(
    private val tmdbApi: TheMovieDb,
    private val converter: DataConverter,
    private val dataBase: AppDatabase,
    private val userDatabase: UserDatabase
) {

    private suspend fun getListMovieFullInfo(page: Int): List<MovieFullInfo> {
        val list = mutableListOf<MovieFullInfo>()
        tmdbApi.getListId(page).results
            .map {
                list.add(tmdbApi.getMovieFullInfo(it.id))
            }
        return list
    }

    suspend fun getActors(id: Int): List<Actor> {
        val list = mutableListOf<Actor>()
        withContext(Dispatchers.IO) {
            tmdbApi.getActors(id).cast.map {
                list.add(Actor(it.id, it.name, BASE_POSTER_URL_PROFILE + it.profilePath))
            }
        }
        return list
    }

    suspend fun getTrailer(id: Int): String {
        return withContext(Dispatchers.IO) {
            BASE_URL_YOUTUBE + tmdbApi.getMovieVideos(id).results[0].key
        }
    }

    suspend fun getListOfFilms2(query: String): List<Movie> {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<MovieFullInfo>()
            tmdbApi.getSearchMovieListId(query).searchMovies.map {
                try {
                    list.add(tmdbApi.getMovieFullInfo(it.id))
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            converter.convertToMovie(list)
        }
    }

    suspend fun getListOfFilms(page: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            converter.convertToMovie(getListMovieFullInfo(page))
        }
    }

    suspend fun getListOfFilmsFromCache(): List<Movie>{
        return withContext(Dispatchers.IO) {
            dataBase.filmDao().getAll()
        }
    }

    suspend fun isCacheEmpty(): Boolean{
        return withContext(Dispatchers.IO) {
            dataBase.filmDao().getAll().isEmpty()
        }
    }

    suspend fun clearCache(){
        return withContext(Dispatchers.IO) {
            dataBase.filmDao().deleteAll()
        }
    }

    suspend fun fillCache(films: List<Movie>){
        return withContext(Dispatchers.IO) {
            dataBase.filmDao().insertAll(films)
        }
    }

    suspend fun getListOfFilmsFromUserDatabase(): List<Movie> {
        return withContext(Dispatchers.IO){
            userDatabase.filmDao().getAll()
        }
    }

    companion object {
        private const val BASE_POSTER_URL_PROFILE = "http://image.tmdb.org/t/p/w185"
        private const val BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v="
    }
}