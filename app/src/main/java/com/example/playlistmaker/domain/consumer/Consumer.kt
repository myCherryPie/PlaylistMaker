package com.example.playlistmaker.domain.consumer

import com.example.playlistmaker.domain.models.Track


interface Consumer {
    fun consume(data: List<Track>)
}