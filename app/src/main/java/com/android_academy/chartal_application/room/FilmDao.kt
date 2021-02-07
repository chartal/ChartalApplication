package com.android_academy.chartal_application.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_academy.chartal_application.data.Movie

@Dao
interface FilmDao {

    @Query("SELECT * FROM films")
    fun getAll(): LiveData<List<Movie>>

    @Query("SELECT * FROM films")
    fun getAllExperiment(): List<Movie>

    @Query("SELECT * FROM films WHERE title LIKE :title   LIMIT 1")
    fun findByName(title: String): Movie

    @Query("DELETE FROM films")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(films: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: Movie?): Long

    @Query("SELECT 1 from films LIMIT 1")
    fun isTableFilmsEmpty(): Int

}
