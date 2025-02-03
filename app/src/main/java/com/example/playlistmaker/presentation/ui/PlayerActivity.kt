package com.example.playlistmaker.presentation.ui

import android.media.MediaPlayer
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {

    private lateinit var play: ImageButton
    private lateinit var timer: TextView
    private lateinit var url: String
    private lateinit var playerRunnable:Runnable
    private val mainThread = Handler(Looper.getMainLooper())
    private val getPlayerRepo = Creator.provideGetPlayerInteractor(play,timer,mainThread)
    private val mediaPlayer = getPlayerRepo.getMediaPlayer()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)

        play = findViewById(R.id.btn_play)
        timer = findViewById(R.id.trackClock)

        val arrowBackFromPlayer = findViewById<ImageButton>(R.id.arrow_back_from_player)
        arrowBackFromPlayer.setOnClickListener {
            finish()
        }
        val track = if (SDK_INT >= 33) {
            intent.getParcelableExtra("track", Track::class.java)!!
        } else {
            intent.getParcelableExtra<Track>("track")!!
        }

            inflatePlayer(track)

        url = track.previewUrl.toString()
        getPlayerRepo.preparePlayer(url,play,timer,mainThread)

        play.setOnClickListener {
            getPlayerRepo.playbackControl(mainThread,play)
        }
        timer.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(STARTING_TIME_TRACK_CLOCK)
        playerRunnable = Runnable {timer.text = getPlayerRepo.updateTimer(mainThread,mediaPlayer,timer).toString() }
    }


    override fun onPause() {
        super.onPause()
        getPlayerRepo.pausePlayer(mainThread,play)
    }
    override fun onDestroy() {
        mainThread.removeCallbacks(playerRunnable)
        getPlayerRepo.playerRelease()
        super.onDestroy()
    }


    private fun inflatePlayer(track: Track) {
        val trackName: TextView = findViewById(R.id.trackName)
        val artistName: TextView = findViewById(R.id.artistName)
        val trackTime: TextView = findViewById(R.id.trackTime)
        val iconTrack: ImageView = findViewById(R.id.iconTrack)
        val trackClock: TextView = findViewById(R.id.trackClock)
        val collectionName: TextView = findViewById(R.id.collectionName)
        val releaseDate: TextView = findViewById(R.id.releaseDate)
        val primaryGenreName: TextView = findViewById(R.id.primaryGenreName)
        val country: TextView = findViewById(R.id.countryTrack)
        val cornerImageTrack = 8f

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackClock.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        if (track.collectionName?.isEmpty() == true) {
            collectionName.visibility = View.GONE
        } else collectionName.text = track.collectionName

        releaseDate.text = track.releaseDate?.substringBefore("-")
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country

        Glide.with(iconTrack)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        cornerImageTrack,
                        resources.displayMetrics
                    ).toInt()
                )
            )
            .into(iconTrack)
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val DELAY_PLAY_CLOCK = 300L
        const val STARTING_TIME_TRACK_CLOCK = 0
    }
}