package com.example.playlistmaker

import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.use_cases.GetTrackListUseCase

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideGetTrackListUseCase(): GetTrackListUseCase {
        return GetTrackListUseCase(getTracksRepository())
    }
}