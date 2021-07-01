package com.josus.itune.repository

import com.josus.itune.api.RetrofitInstance
import com.josus.itune.db.MusicDatabase
import com.josus.itune.model.Music

class MusicRepository(val db:MusicDatabase) {

    suspend fun getAllMusics(query:String)=
        RetrofitInstance.api.getMusic(query)


    suspend fun upsert(music:Music)=db.getMusicDAO().upsert(music)

    fun getSavedMusic()=db.getMusicDAO().getAllMusics()

    fun getSearchedArtist(query: String)=db.getMusicDAO().getArtistBySearch(query)
}