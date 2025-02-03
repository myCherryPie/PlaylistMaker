package com.example.playlistmaker.domain.interactors

import android.media.MediaPlayer
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.domain.api.PlayerRepo

class PlayerInteractorImpl(private val repo: PlayerRepo): PlayerInteractor {
    override fun getMediaPlayer():MediaPlayer {
       return repo.getMediaPlayer()
    }

    override fun playerRelease() {
        repo.playerRelease()
    }
    override fun trackClock(mainThread: Handler) {
        repo.trackClock(mainThread)
    }
    override  fun getStatusPlayer(): Int {
       return repo.getStatusPlayer()
    }

    override fun preparePlayer(url:String, play: ImageButton, timer: TextView, mainThread: Handler) {
        repo.preparePlayer(url,play,timer,mainThread)
    }

    override fun pausePlayer(mainThread: Handler,play: ImageButton) {
        repo.pausePlayer(mainThread,play)
    }

    override fun startPlayer(mainThread: Handler,play: ImageButton) {
        repo.startPlayer(mainThread,play)
    }

    override fun playbackControl(mainThread: Handler,play: ImageButton) {
       repo.playbackControl(mainThread,play)
    }

    override fun updateTimer(mainThread: Handler, mediaPlayer: MediaPlayer, timer: TextView) {
        repo.updateTimer(mainThread,mediaPlayer,timer)
    }
}