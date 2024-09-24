package com.example.playlistmaker

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.AppSP.Companion.TRACK
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity() : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)

        val arrowBackFromPlayer = findViewById<ImageButton>(R.id.arrow_back_from_player)
        arrowBackFromPlayer.setOnClickListener {
            finish()
        }
        val track = Gson().fromJson(
            intent.getStringExtra(TRACK),
            Track::class.java
        )
        inflatePlayer(track)

    }
        fun inflatePlayer(track: Track) {
            val trackName: TextView = findViewById(R.id.trackName)
            val artistName: TextView = findViewById(R.id.artistName)
            val trackTime: TextView = findViewById(R.id.trackTime)
            val iconTrack: ImageView = findViewById(R.id.iconTrack)
            val trackClock:TextView = findViewById(R.id.trackClock)
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

            if (track.collectionName.isEmpty()) {
                collectionName.visibility = View.GONE
            } else collectionName.text = track.collectionName

            releaseDate.text = track.releaseDate.substringBefore("-")
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country

            Glide.with(iconTrack)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
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

}