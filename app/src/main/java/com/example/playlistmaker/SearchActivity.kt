package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R.string
import com.bumptech.glide.Glide
import kotlin.random.Random

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        if (savedInstanceState != null) {
            countValue = savedInstanceState.getString(SEARCH_AMOUNT, AMOUNT_DEF)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editTextSearch = findViewById<EditText>(R.id.edit_text_search)


        val arrowBackFromSearch = findViewById<ImageButton>(R.id.arrow_back_from_search)
        arrowBackFromSearch.setOnClickListener {

            finish()
        }
        val btnCrossClear = findViewById<ImageView>(R.id.clear_cross_search)
        btnCrossClear.setOnClickListener {
            editTextSearch.setText("")
            editTextSearch.clearFocus()
            editTextSearch.isCursorVisible = false
        }

        val textWatcherSearch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                editTextSearch.isCursorVisible = true

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnCrossClear.visibility = visibilityClearButton(s)
                countValue = editTextSearch.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        editTextSearch.addTextChangedListener(textWatcherSearch)
        val track1 = Track(
            getString(string.trackName_1),
            getString(string.artistName_1),
            getString(string.trackTime_1),
            getString(string.artworkUrl100_1)

        )
        val track2 = Track(
            getString(string.trackName_2),
            getString(string.artistName_2),
            getString(string.trackTime_2),
            getString(string.artworkUrl100_2)
        )

        val track3 = Track(
            getString(string.trackName_3),
            getString(string.artistName_3),
            getString(string.trackTime_3),
            getString(string.artworkUrl100_3)
        )
        val track4 = Track(
            getString(string.trackName_4),
            getString(string.artistName_4),
            getString(string.trackTime_4),
            getString(string.artworkUrl100_4)
        )
        val track5 = Track(
            getString(string.trackName_5),
            getString(string.artistName_5),
            getString(string.trackTime_5),
            getString(string.artworkUrl100_5)
        )
        var tracks = mutableListOf<Track>(track1,track2,track3,track4,track5)
        val tracksAdapter = TrackAdapter(tracks)
        val recyclerViewTrack = findViewById<RecyclerView>(R.id.recyclerTracks)
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)
        recyclerViewTrack.adapter = tracksAdapter


    }

    private var countValue: String = AMOUNT_DEF
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_AMOUNT, countValue)
    }

    private fun visibilityClearButton(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    companion object {
        const val SEARCH_AMOUNT = "SEARCH_AMOUNT"
        const val AMOUNT_DEF = ""
    }
}

