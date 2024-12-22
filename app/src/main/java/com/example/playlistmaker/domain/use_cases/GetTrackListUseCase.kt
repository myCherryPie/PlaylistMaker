package com.example.playlistmaker.domain.use_cases

import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.consumer.Consumer
import java.util.concurrent.Executors

class GetTrackListUseCase (private val repository: TracksRepository) {
    private val executor = Executors.newCachedThreadPool()
    fun searchTracks(expression: String, consumer: Consumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
           }
        }
    }