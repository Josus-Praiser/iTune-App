package com.josus.itune.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josus.itune.model.Music
import com.josus.itune.model.MusicResponse
import com.josus.itune.repository.MusicRepository
import com.josus.itune.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MusicViewModel(
    private val repository: MusicRepository):ViewModel() {

    val searchedMusics:MutableLiveData<Resource<MusicResponse>> = MutableLiveData()


    fun getSearchMusic(query:String)=viewModelScope.launch {
        val response=repository.getAllMusics(query)
        searchedMusics.postValue(handleSearchMusicResponse(response))
    }

        private fun handleSearchMusicResponse(response: Response<MusicResponse>):Resource<MusicResponse>{
            if (response.isSuccessful){
                response.body()?.let { resultResponse->
                    return Resource.Success(resultResponse)
                }
            }
            return Resource.Error(response.message())
        }

    fun saveMusic(music: Music)=viewModelScope.launch {
        repository.upsert(music)
    }

    fun getSavedMusic()=repository.getSavedMusic()

    fun getArtistBySearch(query: String)=repository.getSearchedArtist(query)
}