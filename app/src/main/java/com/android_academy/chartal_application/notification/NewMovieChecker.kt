package com.android_academy.chartal_application.notification


import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.android_academy.chartal_application.App
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.repository.NetworkModule
import kotlinx.coroutines.*

private const val LOG_TAG = "Chartal"

class NewMovieChecker {
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }
    private var scope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.IO +
                exceptionHandler
    )
    private val context = App.instance
    private val repository = NetworkModule.filmsRepository
    private val oldCacheMovieList = mutableListOf<Movie>()
    private val newCacheMovieList = mutableListOf<Movie>()
    private val notifications = AndroidNotifications(context)

    init {
        scope.launch { oldCacheMovieList.addAll(repository.getListOfFilmsFromCache()) }
    }

    private suspend fun getMovieForNotification(): Pair<Movie, Boolean> {
        var newMovieFlag: Boolean
        var movie: Movie
        newCacheMovieList.apply {
            clear()
            addAll(repository.getListOfFilmsFromCache())
        }
        val differenceList = newCacheMovieList.asSequence().minus(oldCacheMovieList).map { it }.toList()
        oldCacheMovieList.apply {
            clear()
            addAll(newCacheMovieList)
        }
        if(differenceList.isEmpty()){
            newMovieFlag = false
            movie = newCacheMovieList.maxByOrNull { it.ratings } ?: newCacheMovieList[0]
            Log.d(LOG_TAG, "There are no new films in the database")
        }else{
            newMovieFlag = true
            movie =  differenceList.maxByOrNull { it.ratings } ?: newCacheMovieList[0]
            Log.d(LOG_TAG, "there is new film in the database: $movie")
        }
        return Pair(movie, newMovieFlag)
    }

    suspend fun showNotification() {
        val pair = this.getMovieForNotification()
        val movie = pair.first
        val isNewMovie = pair.second
        val titleForNotification = if (isNewMovie) {
            context.getString(R.string.new_movie_in_the_database)
        } else {
            context.getString(R.string.movie_with_highest_rating)
        }
        if (isChannelEnabled(NotificationManagerCompat.from(context), CHANNEL_ID)) {
            notifications.showNotification(movie, titleForNotification)

        }else{
            Log.d(LOG_TAG, "The channel for displaying the notification is disabled")
        }

    }

    private fun isChannelEnabled(nm: NotificationManagerCompat, id: String): Boolean {
        val ch = nm.getNotificationChannel(id)
        return ch != null && ch.importance != NotificationManager.IMPORTANCE_NONE
    }
}