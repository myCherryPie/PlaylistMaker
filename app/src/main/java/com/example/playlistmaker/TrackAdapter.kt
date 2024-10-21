package com.example.playlistmaker

import com.google.gson.Gson
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
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
        val searchAct = SearchActivity()
        val  tr = tracks[position]
        val trackPosition = Gson().toJson(tr)
        holder.bind(tr, listener)
        holder.itemView.setOnClickListener {

                listener.onClick(tr)
                if(searchAct.getClickDebounce()) {
                val player = Intent(it.context, PlayerActivity::class.java)
                player.putExtra(TRACK, trackPosition )
                it.context.startActivity(player)
            }
        }
    }

    }

