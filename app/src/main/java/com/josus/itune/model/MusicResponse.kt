package com.josus.itune.model

data class MusicResponse(
    val resultCount: Int,
    val results: List<Music>
)