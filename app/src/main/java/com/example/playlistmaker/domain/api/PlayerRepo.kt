package com.example.playlistmaker.domain.api

import android.media.MediaPlayer
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView


interface PlayerRepo {
    fun getMediaPlayer():MediaPlayer
    fun playerRelease()
    fun getStatusPlayer(): Int
    fun preparePlayer(url:String, play: ImageButton, timer: TextView, mainThread: Handler)
    fun pausePlayer(mainThread: Handler,play: ImageButton)
    fun startPlayer(mainThread: Handler,play: ImageButton)
    fun playbackControl(mainThread: Handler,play: ImageButton)
    fun updateTimer(mainThread: Handler,mediaPlayer: MediaPlayer,timer: TextView)
    fun trackClock(mainThread:Handler)
}