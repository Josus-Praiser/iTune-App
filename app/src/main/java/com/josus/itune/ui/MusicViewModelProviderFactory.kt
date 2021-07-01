package com.josus.itune.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josus.itune.repository.MusicRepository

class MusicViewModelProviderFactory(
   private val musicRepository: MusicRepository
) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        @Suppress("UNCHECKED_CAST")
        return MusicViewModel(musicRepository) as T
    }
}