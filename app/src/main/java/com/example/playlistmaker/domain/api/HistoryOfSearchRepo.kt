package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryOfSearchRepo {
    fun getHistoryOfSearch(): ArrayList<Track>
    fun saveHistoryOfSearch(tracks : ArrayList<Track>)
    fun getHistoryFromGson(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun cleanHistoryOfSearch()

}