package com.example.playlistmaker


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.AppSP.Companion.TRACK

class TrackAdapter( private val tracks: List<Track>,private val listener: ClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val trackPosition  = tracks[position]
        holder.bind(trackPosition, listener)
        holder.itemView.setOnClickListener {
            listener.onClick(trackPosition)
            val player = Intent(it.context,PlayerActivity::class.java)
           player.putExtra(TRACK,trackPosition.toString())
            startActivity(it.context,player, player.extras)
        }
    }
}
