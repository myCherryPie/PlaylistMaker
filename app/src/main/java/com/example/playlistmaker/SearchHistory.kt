package com.example.playlistmaker

import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class SearchHistory (private val sharedPrefs : AppSP) {
    private var tracks : ArrayList<Track> = getHistoryOfSearch()
    private fun saveHistoryOfSearch(tracks : ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit(HISTORY_TRACK,json)
    }

    private fun getHistoryOfSearch(): ArrayList<Track> {
        val json = sharedPrefs.getString(HISTORY_TRACK) ?: return ArrayList()
        val token = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, token)
    }
    fun addTrackToList(track: Track) {
        if (tracks.isEmpty()) {
            tracks.add(track)
            saveHistoryOfSearch(tracks)
            return
        }
        if (tracks.isNotEmpty()) {
            for (item in tracks) {
                if (item.trackId.equals(track.trackId)) {
                    tracks.remove(item)
                    tracks.add(0, track)
                    saveHistoryOfSearch(tracks)
                    return
                }
            }
        }
        if (tracks.size < MAX_SIZE_LIST) {
            tracks.add(0, track)
        } else {
            tracks.removeLast()
            tracks.add(0, track)
        }
        saveHistoryOfSearch(tracks)
    }

    fun getTracks(): ArrayList<Track> {
        return tracks
    }

    fun cleanList() {
        tracks.clear()
        saveHistoryOfSearch(tracks)
    }

    companion object {
        const val MAX_SIZE_LIST = 10
        const val HISTORY_TRACK = "history_track"
    }
}