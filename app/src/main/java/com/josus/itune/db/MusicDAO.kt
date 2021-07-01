 package com.josus.itune.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.josus.itune.model.Music

@Dao
interface MusicDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(music:Music):Long

    @Query("SELECT * FROM musics")
    fun getAllMusics():LiveData<List<Music>>

    @Query("SELECT * FROM musics WHERE artistName LIKE :query")
    fun getArtistBySearch(query: String):LiveData<List<Music>>

}