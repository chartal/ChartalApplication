package com.android_academy.chartal_application.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android_academy.chartal_application.data.ActorDb
import com.android_academy.chartal_application.data.ListId

@Dao
interface ActorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(ids: ListId?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActors(actors: List<ActorDb>?)

    @Query("SELECT * FROM ids WHERE movieId =:movieId")
    fun getList(movieId: Int): ListId

    @Query("SELECT * FROM actors WHERE movieId =:movieId")
    fun getActorDbList(movieId: Int): List<ActorDb>

    fun insertListWithActorDb(list: ListId) {
        var actors: List<ActorDb>? = list.actors
        for (i in actors!!.indices) {
            actors[i].movieId = list.movieId
        }
        insertActors(actors)
        insertList(list)
    }

    fun getListWithActorsDb(id: Int): ListId? {
        val list: ListId = getList(id)
        val actors1: List<ActorDb> = getActorDbList(id)
        list.actors = actors1
        return list
    }

}
