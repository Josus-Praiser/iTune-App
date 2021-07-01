package com.josus.itune.api

import com.josus.itune.model.MusicResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicAPI {


    @GET("/search?")
    suspend fun getMusic(
        @Query("term")
        searchQuery:String,
        @Query("media")
        searchType:String="music",
        @Query("country")
        country:String="in"
    ):Response<MusicResponse>

}
// /search?term=dusk&media=music