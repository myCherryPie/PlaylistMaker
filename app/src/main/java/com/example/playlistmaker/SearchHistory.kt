package com.example.playlistmaker

class SearchHistory (private val sharedPrefs : AppSP) {
    private var tracks : ArrayList<Track> = sharedPrefs.getHistoryOfSearch()

    fun addTrackToList(track: Track) {
        if (tracks.isEmpty()) {
            tracks.add(track)
            sharedPrefs.saveHistoryOfSearch(tracks)
            return
        }
        if (tracks.isNotEmpty()) {
            for (item in tracks) {
                if (item.trackId.equals(track.trackId)) {
                    tracks.remove(item)
                    tracks.add(0, track)
                    sharedPrefs.saveHistoryOfSearch(tracks)
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
        sharedPrefs.saveHistoryOfSearch(tracks)
    }

    fun getTracks(): ArrayList<Track> {
        return tracks
    }

    fun cleanList() {
        tracks.clear()
        sharedPrefs.saveHistoryOfSearch(tracks)
    }

    companion object {
        const val MAX_SIZE_LIST = 10
    }
}