package com.android_academy.chartal_application.repository


import android.util.Log
import com.android_academy.chartal_application.api.MovieFullInfo
import com.android_academy.chartal_application.api.TheMovieDb
import com.android_academy.chartal_application.data.*
import com.android_academy.chartal_application.room.AppDatabase
import com.android_academy.chartal_application.util.DataConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val LOG_TAG = "Chartal"

class FilmsRepository(
    private val tmdbApi: TheMovieDb,
    private val converter: DataConverter,
    private val dataBase: AppDatabase
) {

    private suspend fun getListMovieFullInfo(page: Int): List<MovieFullInfo> {
        val list = mutableListOf<MovieFullInfo>()
        tmdbApi.getListId(page).results
            .map {
                list.add(tmdbApi.getMovieFullInfo(it.id))
            }
        return list
    }

    private suspend fun getListOfPopularId(): List<Int> {
        val list = mutableListOf<Int>()
        tmdbApi.getListId(1).results
            .map {
                list.add(it.id)
            }
        return list
    }

    suspend fun updateActorsCache() {
        withContext(Dispatchers.IO) {
            val list = getListOfPopularId()
            list.forEach {
                getActorsAndSaveInCache(it)
            }
            Log.d(LOG_TAG, "Updating the Actor list cache")
        }
    }

    suspend fun getActorsAndSaveInCache(id: Int): List<Actor> {
        val list = mutableListOf<Actor>()
        withContext(Dispatchers.IO) {
            tmdbApi.getActors(id).cast.take(8).map {
                list.add(Actor(it.id, it.name, BASE_POSTER_URL_PROFILE + it.profilePath))
            }
        }
        withContext(Dispatchers.IO) {
            val actorDbList = mutableListOf<ActorDb>()
            list.forEach {
                actorDbList.add(ActorDb(it.id, it.name, it.picture, id))
            }
            val listId = ListId(id, actorDbList)
            Log.d(LOG_TAG, "The list  downloaded from network and saved in the cache: $listId")
            dataBase.actorDao().insertListWithActorDb(listId)
        }
        return list
    }

    suspend fun getActorsFromCache(id: Int): List<Actor> {
        val actorList = mutableListOf<Actor>()
        withContext(Dispatchers.IO) {
            val actorsDb = dataBase.actorDao().getListWithActorsDb(id)?.actors
            actorsDb?.forEach {
                actorList.add(Actor(it.id, it.name, it.picture))
            }
            Log.d(LOG_TAG, "the list is loaded from the cache: $actorList")
        }
        return actorList
    }

    suspend fun getTrailer(id: Int): String {
        return withContext(Dispatchers.IO) {
            BASE_URL_YOUTUBE + tmdbApi.getMovieVideos(id).results[0].key
        }
    }

    suspend fun getSearchMovies(query: String): List<Movie> {
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
            Log.d(LOG_TAG, "Making a request to the network")
            converter.convertToMovie(getListMovieFullInfo(page))
        }
    }

    suspend fun getListOfFilmsFromCache(): List<Movie> {
        return withContext(Dispatchers.IO) {
            dataBase.filmDao().getAllExperiment()
        }
    }

    suspend fun isCacheEmpty(): Boolean {
        return withContext(Dispatchers.IO) {
            when (dataBase.filmDao().isTableFilmsEmpty()) {
                1 -> false
                else -> true
            }
        }
    }

    suspend fun fillCache(films: List<Movie>) {
        return withContext(Dispatchers.IO) {
            dataBase.filmDao().deleteAll()
            dataBase.filmDao().insertAll(films)
        }
    }

    suspend fun getListOfFilmsFromUserDatabase(): List<Movie> {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<MovieFullInfo>()
            dataBase.filmUserDao().getAll()
                .forEach {
                    try {
                        list.add(tmdbApi.getMovieFullInfo(it.id!!))
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            converter.convertToMovie(list)
        }

    }

    suspend fun saveUserMovie(movie: Movie?) {
        withContext(Dispatchers.IO) {
            dataBase.filmUserDao().insert(UserMovie(movie?.id, movie?.title))
        }
    }

    suspend fun deleteUserMovie(movie: Movie?) {
        withContext(Dispatchers.IO) {
            dataBase.filmUserDao().deleteById(movie?.id)
        }
    }

    suspend fun updateCach() {
        dataBase.filmDao().deleteAll()
        dataBase.filmDao().insertAll(getListOfFilms(1))
    }

    companion object {
        private const val BASE_POSTER_URL_PROFILE = "http://image.tmdb.org/t/p/w185"
        private const val BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v="
    }
}