package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity() {
    private lateinit var play: ImageButton
    private lateinit var timer: TextView
    private lateinit var url: String
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var playerRunnable:Runnable
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
        val track: Track? = intent.getParcelableExtra<Parcelable>(
            Track::class.java.canonicalName
        ) as Track?

        if (track != null) {
            inflatePlayer(track)
        }
        url = track?.previewUrl.toString()
        preparePlayer()
        play.setOnClickListener {
            playbackControl()
        }
        timer.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(STARTING_TIME_TRACK_CLOCK)
        playerRunnable = Runnable {updateTimer()}


    }
    private val mainThread = Handler(Looper.getMainLooper())

     private fun trackClock() {
         mainThread.post(playerRunnable)
     }
    private fun updateTimer() {
                if(playerState != STATE_PREPARED) {
                    timer.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition.toLong())
                    mainThread.postDelayed(playerRunnable, DELAY_PLAY_CLOCK)
                }else {
                    timer.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(STARTING_TIME_TRACK_CLOCK)
                }
            }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        mainThread.removeCallbacks(playerRunnable)
        mediaPlayer.release()
        super.onDestroy()
    }
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        trackClock()
    }
    private fun pausePlayer() {
        mediaPlayer.pause()
        mainThread.removeCallbacks(playerRunnable)
        play.setImageResource(R.drawable.playbtn)
        playerState = STATE_PAUSED
    }
    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setImageResource(R.drawable.playbtn)
            timer.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(STARTING_TIME_TRACK_CLOCK)
            playerState = STATE_PREPARED
            mainThread.removeCallbacks(playerRunnable)
        }
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
        val cornerImageTrack = 2f

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
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        const val DELAY_PLAY_CLOCK = 300L
        const val STARTING_TIME_TRACK_CLOCK = 0
    }
}