package com.example.playlistmaker

import android.content.res.Resources
import android.content.res.Resources.getSystem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

val Int.dp: Int get() = (this / getSystem().displayMetrics.density).toInt()
val cor = 2.dp
class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val iconTrack: ImageView = itemView.findViewById(R.id.iconTrack)

    fun bind(model: Track) {

        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(cor))
            .into(iconTrack)
    }

}