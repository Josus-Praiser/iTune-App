package com.josus.itune.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.josus.itune.getOrAwaitValue
import com.josus.itune.model.Music
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class MusicDaoTest {

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()

    private lateinit var database: MusicDatabase
    private lateinit var dao: MusicDAO

    @Before
    fun setup(){
        database= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MusicDatabase::class.java
        ).allowMainThreadQueries().build()

        dao=database.getMusicDAO()

    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertMusicItems()= runBlockingTest{
        val musicItem=Music(1,234,"Joss","https://jos","imageurl")
        dao.upsert(musicItem)

        val allMusicItem=dao.getAllMusics().getOrAwaitValue()

        assertThat(allMusicItem).contains(musicItem)
    }

}