package com.example.playlistmaker.data

import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.SearchRequest
import com.example.playlistmaker.data.dto.SearchResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(SearchRequest(expression))
        val code = response.resultCode
        if(response is SearchResponse && code == 200) {
            return response.results.map{
               Track(it.trackName,
                   it.artistName,
                   it.trackTimeMillis,
                   it.artworkUrl100,
                   it.trackId,
                   it.collectionName,
                   it.releaseDate,
                   it.primaryGenreName,
                   it.country,
                   it.previewUrl)}
        }
       else {
           return emptyList()
        }
    }
}