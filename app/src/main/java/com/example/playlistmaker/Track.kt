package com.example.playlistmaker

data class Track (
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: Int,
    val primaryGenreName: String,
    val country: String
) {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")


}




