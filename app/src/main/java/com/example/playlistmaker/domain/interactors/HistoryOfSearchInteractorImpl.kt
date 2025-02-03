package com.example.playlistmaker.domain.interactors

import com.example.playlistmaker.domain.api.HistoryOfSearchRepo
import com.example.playlistmaker.domain.models.Track

class HistoryOfSearchInteractorImpl(private val repo:HistoryOfSearchRepo):HistoryOfSearchInteractor {
    override fun getHistoryOfSearch(): ArrayList<Track> {
        return repo.getHistoryOfSearch()
    }

    override fun addTrackToHistory(track: Track) {
        repo.addTrackToHistory(track)
    }

    override fun cleanHistoryOfSearch() {
        repo.cleanHistoryOfSearch()
    }

}