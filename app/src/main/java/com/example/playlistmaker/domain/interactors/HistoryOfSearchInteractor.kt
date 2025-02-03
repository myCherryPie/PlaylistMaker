package com.example.playlistmaker.domain.interactors

import com.example.playlistmaker.domain.models.Track

interface HistoryOfSearchInteractor {
    fun getHistoryOfSearch(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
    fun cleanHistoryOfSearch()
}