package com.android_academy.chartal_application.backgroundwork

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android_academy.chartal_application.notification.NewMovieChecker
import com.android_academy.chartal_application.repository.NetworkModule



private const val LOG_TAG = "Chartal"

class UpdateCacheWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val newMovieChecker = NewMovieChecker()
        if (ConnectionChecker.isOnline()) {
            Log.d(LOG_TAG, "ConnectionChecker is online")
            val repository = NetworkModule.filmsRepository
            repository.apply {
                updateCache()
                updateActorsCache()
            }
            Log.d(LOG_TAG, "The cache has been updated by workManager")
            newMovieChecker.showNotification()
            return Result.success()
        }
        return Result.failure()
    }

}