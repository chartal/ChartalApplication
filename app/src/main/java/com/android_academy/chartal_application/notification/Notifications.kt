package com.android_academy.chartal_application.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.android_academy.chartal_application.MainActivity
import com.android_academy.chartal_application.R
import com.android_academy.chartal_application.data.Movie

interface Notifications {
    fun initialize()
    fun showNotification(movie: Movie, message: String)
    fun dismissNotification(chatId: Long)
}

class AndroidNotifications(private val context: Context) : Notifications {

    companion object {
        private const val REQUEST_CONTENT = 1
        private const val CHAT_TAG = "chat"
    }

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
        initialize()
    }

    override fun initialize() {
        createChannel(context)
    }

    @WorkerThread
    override fun showNotification(movie: Movie, message: String) {
        val contentUri = "https://www.themoviedb.org/movie/${movie.id}".toUri()
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(message)
            .setContentText(movie.title)
            .setSmallIcon(R.drawable.ic_baseline_personal_video_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

        notificationManagerCompat.notify(CHAT_TAG, movie.id, builder.build())
    }

    override fun dismissNotification(chatId: Long) {
        notificationManagerCompat.cancel(CHAT_TAG, chatId.toInt())
    }
}