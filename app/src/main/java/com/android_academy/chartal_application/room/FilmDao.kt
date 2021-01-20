package com.android_academy.chartal_application.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_academy.chartal_application.data.MovieDb

@Dao
interface FilmDao {

    @Query("SELECT * FROM Films")
    fun getAll(): LiveData<List<MovieDb>>

    @Query("SELECT * FROM films")
    fun getAllExperiment(): List<MovieDb>

    @Query("SELECT * FROM films WHERE title LIKE :title   LIMIT 1")
    fun findByName(title: String): MovieDb

    @Query("DELETE FROM films")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(films: List<MovieDb>)

    @Insert
    fun insert(picture: MovieDb): Long
}
