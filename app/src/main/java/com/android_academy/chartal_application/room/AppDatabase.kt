package com.android_academy.chartal_application.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android_academy.chartal_application.data.ActorDb
import com.android_academy.chartal_application.data.ListId
import com.android_academy.chartal_application.data.Movie
import com.android_academy.chartal_application.data.UserMovie

@Database(entities = [Movie::class, UserMovie::class, ListId::class, ActorDb::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao
    abstract fun actorDao(): ActorDao
    abstract fun filmUserDao(): FilmUserDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Films.db"
                ).fallbackToDestructiveMigration().
                build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

