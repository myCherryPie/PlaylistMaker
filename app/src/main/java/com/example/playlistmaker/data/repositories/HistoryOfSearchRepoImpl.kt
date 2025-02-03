package com.example.playlistmaker.data.repositories

import android.content.SharedPreferences
import com.example.playlistmaker.domain.api.HistoryOfSearchRepo
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryOfSearchRepoImpl (private val sharedPrefs : SharedPreferences) : HistoryOfSearchRepo {
     private var tracks : ArrayList<Track> = getHistoryFromGson()

    override fun saveHistoryOfSearch(tracks : ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit().putString(HISTORY_TRACK,json).apply()
    }
    override fun getHistoryOfSearch(): ArrayList<Track> {
        return tracks
    }
    override fun getHistoryFromGson(): ArrayList<Track> {
        val json = sharedPrefs.getString(HISTORY_TRACK,null) ?: return ArrayList()
        val token = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, token)
    }
    override fun addTrackToHistory(track: Track) {
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

   override fun cleanHistoryOfSearch() {
        tracks.clear()
        saveHistoryOfSearch(tracks)
    }

    companion object {
        const val MAX_SIZE_LIST = 10
        const val HISTORY_TRACK = "history_track"
    }
}