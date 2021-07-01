package com.josus.itune.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.josus.itune.model.Music


@Database(entities = [Music::class],
version = 1)

abstract class MusicDatabase:RoomDatabase() {

    abstract fun getMusicDAO():MusicDAO

    companion object{
        @Volatile
        private var instance:MusicDatabase?=null
        private var LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(context.applicationContext,
                MusicDatabase::class.java,
            "music_db.db").build()
    }
}