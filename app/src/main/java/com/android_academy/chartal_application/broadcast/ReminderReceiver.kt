package com.android_academy.chartal_application.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android_academy.chartal_application.details.DetailsMovieFragment.Companion.MOVIE_ID
import com.android_academy.chartal_application.notification.AndroidNotifications
import com.android_academy.chartal_application.repository.NetworkModule
import kotlinx.coroutines.*


class ReminderReceiver : BroadcastReceiver() {


    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }
    private var scope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.IO +
                exceptionHandler
    )
    override fun onReceive(context: Context, intent: Intent) {
        scope.launch {
            val id = intent.getIntExtra(MOVIE_ID, 0)
            val movie = NetworkModule.filmsRepository.findMovieById(id)
            AndroidNotifications(context).showNotification(
                movie,
                "You have scheduled to watch this movie:"
            )
        }
    }
}