package com.example.playlistmaker

class SearchRequest (val text: String) {
    data class RequestContent (
       val trackName: String,
        val artistName: String,
        val trackTimeMillis: String,
        val artworkUrl100: String
    )
}