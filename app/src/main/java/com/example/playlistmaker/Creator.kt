package com.example.playlistmaker

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.domain.api.HistoryOfSearchRepo
import com.example.playlistmaker.data.repositories.HistoryOfSearchRepoImpl
import com.example.playlistmaker.data.repositories.SettingsRepositoryImpl
import com.example.playlistmaker.data.repositories.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repositories.PlayerRepoImpl
import com.example.playlistmaker.domain.api.PlayerRepo
import com.example.playlistmaker.domain.api.SettingsRepo
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.interactors.HistoryOfSearchInteractor
import com.example.playlistmaker.domain.interactors.HistoryOfSearchInteractorImpl
import com.example.playlistmaker.domain.interactors.PlayerInteractorImpl
import com.example.playlistmaker.domain.interactors.PlayerInteractor
import com.example.playlistmaker.domain.interactors.SettingsInteractor
import com.example.playlistmaker.domain.interactors.SettingsInteractorImpl
import com.example.playlistmaker.domain.use_cases.GetTrackListUseCase

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideGetTrackListUseCase(): GetTrackListUseCase {
        return GetTrackListUseCase(getTracksRepository())
    }

    fun getHistoryOfSearchRepository(context: Context): HistoryOfSearchRepo {
        val sharedPrefs = context.getSharedPreferences(SettingsRepositoryImpl.PRACTICUM_EXAMPLE_PREFERENCES, Context.MODE_PRIVATE)
        return HistoryOfSearchRepoImpl(sharedPrefs)
    }

    fun provideGetHistoryOfSearchInteractor(context: Context) : HistoryOfSearchInteractor {
        return HistoryOfSearchInteractorImpl(getHistoryOfSearchRepository(context))
    }

    fun getSettingsRepo(context: Context):SettingsRepo{
        val sharedPrefs = context.getSharedPreferences(SettingsRepositoryImpl.PRACTICUM_EXAMPLE_PREFERENCES, Context.MODE_PRIVATE)
        return SettingsRepositoryImpl(sharedPrefs)
    }

    fun provideGetSettingInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepo(context))
    }

    fun getPlayerRepo(play:ImageButton,timer:TextView,mainThread:Handler):PlayerRepo{
        return PlayerRepoImpl(play,timer,mainThread)
    }

    fun provideGetPlayerInteractor(play:ImageButton,timer:TextView,mainThread:Handler):PlayerInteractor{
        return PlayerInteractorImpl(getPlayerRepo(play,timer,mainThread))
    }
}