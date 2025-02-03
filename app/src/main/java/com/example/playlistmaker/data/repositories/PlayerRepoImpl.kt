package com.example.playlistmaker.data.repositories

import android.media.MediaPlayer
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.PlayerRepo
import com.example.playlistmaker.presentation.ui.PlayerActivity
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepoImpl(play: ImageButton, timer: TextView, mainThread:Handler) : PlayerRepo {
    private var playerState = PlayerActivity.STATE_DEFAULT
    private lateinit var playerRunnable:Runnable
    private var mediaPlayer = MediaPlayer()

    override  fun getMediaPlayer():MediaPlayer{
        return mediaPlayer
    }
    override fun trackClock(mainThread: Handler) {
        mainThread.post(playerRunnable)
    }
    override fun updateTimer(mainThread: Handler,mediaPlayer: MediaPlayer,timer: TextView) {
        if(playerState != PlayerActivity.STATE_PREPARED) {
             SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.currentPosition.toLong())
            mainThread.postDelayed(playerRunnable, PlayerActivity.DELAY_PLAY_CLOCK)
        }else {
            timer.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(PlayerActivity.STARTING_TIME_TRACK_CLOCK)
        }
    }
        override fun playerRelease(){
            mediaPlayer.release()
        }
    override fun playbackControl(mainThread: Handler,play: ImageButton) {
        when(playerState) {
            PlayerActivity.STATE_PLAYING -> {
                pausePlayer(mainThread,play)
            }
            PlayerActivity.STATE_PREPARED, PlayerActivity.STATE_PAUSED -> {
                startPlayer(mainThread,play)
            }
        }
    }
    override fun startPlayer(mainThread: Handler,play: ImageButton) {
        mediaPlayer.start()
        play.setImageResource(R.drawable.pause)
        playerState = PlayerActivity.STATE_PLAYING
        trackClock(mainThread)
    }
    override fun pausePlayer(mainThread: Handler,play: ImageButton) {
        mediaPlayer.pause()
        mainThread.removeCallbacks(playerRunnable)
        play.setImageResource(R.drawable.playbtn)
        playerState = PlayerActivity.STATE_PAUSED
    }
    override fun getStatusPlayer(): Int {
        return playerState
    }
    override fun preparePlayer(url:String,play: ImageButton,timer: TextView,mainThread: Handler) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = PlayerActivity.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setImageResource(R.drawable.playbtn)
            timer.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(PlayerActivity.STARTING_TIME_TRACK_CLOCK)
            playerState = PlayerActivity.STATE_PREPARED
            mainThread.removeCallbacks(playerRunnable)
        }
    }
}