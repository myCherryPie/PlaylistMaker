package com.example.playlistmaker

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
data class Track (
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val iconTrack: String
)

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val artistName: TextView = itemView.findViewById(R.id.artistName)
        private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
        private val iconTrack: TextView = itemView.findViewById(R.id.iconTrack)

        fun bind(model: Track) {
            trackName.text = model.trackName
            artistName.text = model.artistName
            trackTime.text = model.trackTime
            iconTrack.text = model.iconTrack
        }

    }

    class TrackAdapter( private val tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
            return TrackViewHolder(view)
        }

        override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
            holder.bind(tracks[position])

        }

        override fun getItemCount() = tracks.size
    }





