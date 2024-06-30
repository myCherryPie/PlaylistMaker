package com.example.playlistmaker

data class TrackModelServer(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackId: Int
)
