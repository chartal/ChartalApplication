package com.android_academy.chartal_application.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_academy.chartal_application.data.UserMovie

@Dao
interface FilmUserDao {

    @Query("SELECT * FROM userfilms")
    fun getAll(): List<UserMovie>

    @Query("SELECT * FROM userfilms")
    fun getAllExperiment(): List<UserMovie>

    @Query("SELECT * FROM userfilms WHERE title LIKE :title   LIMIT 1")
    fun findByName(title: String): UserMovie

    @Query("DELETE FROM userfilms")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(films: List<UserMovie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: UserMovie?): Long

    @Query("SELECT count(*) from userfilms")
    fun isDatabaseEmpty(): Int

    @Query("SELECT 1 from userfilms LIMIT 1")
    fun isDatabaseEmpty2(): Int

}
