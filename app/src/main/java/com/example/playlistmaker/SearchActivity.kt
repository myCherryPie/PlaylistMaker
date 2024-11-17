package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private lateinit var  tracksHistory : ArrayList<Track>
    private lateinit var tracksAdapterHistory : TrackAdapter
    private var tracks = ArrayList<Track>()
    private lateinit var searchH : SearchHistory
    private lateinit var searchRunnable: Runnable
    private var handlerMainThread = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val onClick:(Track)->Unit = { track -> onClick(track)
    searchH.addTrackToList(track)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        searchH = SearchHistory(applicationContext as AppSP)
        tracks = mutableListOf<Track>() as ArrayList<Track>
        tracksHistory = searchH.getTracks()
        val tracksAdapter = TrackAdapter(onClick,tracks)
        tracksAdapterHistory = TrackAdapter(onClick,tracksHistory)

        val urlItunesApi = getString(R.string.base_url_itunes)
        val retrofit = Retrofit.Builder()
            .baseUrl(urlItunesApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val playListService = retrofit.create(ItunseApi::class.java)
        val placeImgLinkErr = findViewById<ImageView>(R.id.image_error_link)
        val placeTextError = findViewById<TextView>(R.id.text_error_search)
        val placeImgSearchErr = findViewById<ImageView>(R.id.image_error_search)
        val editTextSearch = findViewById<EditText>(R.id.edit_text_search)
        val btnUpdateSearch = findViewById<Button>(R.id.btn_update_search)
        val btnInputClear = findViewById<ImageView>(R.id.clear_cross_search)
        val arrowBackFromSearch = findViewById<ImageButton>(R.id.arrow_back_from_search)
        val btnClearHistory = findViewById<Button>(R.id.btn_clear_history_search)
        val layoutOfHistory = findViewById<LinearLayout>(R.id.layout_history_search)!!
        val progressOfSearch = findViewById<ProgressBar>(R.id.progressOfSearch)




        if (savedInstanceState != null) {
            countValue = savedInstanceState.getString(SEARCH_AMOUNT, AMOUNT_DEF)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        arrowBackFromSearch.setOnClickListener {
            finish()
        }

        btnInputClear.setOnClickListener {
            tracks.clear()
            editTextSearch.setText("")
            editTextSearch.isCursorVisible = false
            btnUpdateSearch.visibility = View.GONE
            placeImgSearchErr.visibility = View.GONE
            placeImgLinkErr.visibility = View.GONE
            placeTextError.visibility = View.GONE
            editTextSearch.clearFocus()
            tracksAdapter.notifyDataSetChanged()
        }
        fun updateRecyclerHistory(){
            layoutOfHistory.visibility = View.VISIBLE
            tracksHistory = searchH.getTracks()
            tracksAdapterHistory.notifyDataSetChanged()
        }
        fun hideLayoutHistory(){
            tracks.clear()
            layoutOfHistory.visibility = View.GONE
        }
        btnClearHistory.setOnClickListener {
            hideLayoutHistory()
            searchH.cleanList()
        }

        editTextSearch.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus && editTextSearch.text.isEmpty() && tracksHistory.isNotEmpty()) {
                updateRecyclerHistory()
            } else hideLayoutHistory()
        }

        val textWatcherSearch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                editTextSearch.isCursorVisible = true
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                btnInputClear.visibility = visibilityClearButton(s)
                editTextSearch.isCursorVisible = true
                countValue = editTextSearch.toString()
                layoutOfHistory.visibility =
                    if(editTextSearch.hasFocus()
                        && s?.isEmpty() == true
                        && tracks.isNotEmpty()) View.VISIBLE else View.GONE
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }

        val recyclerViewTrack = findViewById<RecyclerView>(R.id.recyclerTracks)
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)
        recyclerViewTrack.adapter = tracksAdapter

        val recyclerViewHistory = findViewById<RecyclerView>(R.id.recycler_search_history)
        recyclerViewHistory.layoutManager = LinearLayoutManager(this)
        recyclerViewHistory.adapter = tracksAdapterHistory

        fun findByInput() {
            recyclerViewTrack.visibility = View.GONE
            if (editTextSearch.text.isNotEmpty()) {
                progressOfSearch.visibility = View.VISIBLE
                playListService.search(editTextSearch.text.toString()).enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            val response = response.body()?.results
                            if (response != null) {
                                if (response.isNotEmpty()) {
                                    tracks.addAll(response)
                                    tracksAdapter.notifyDataSetChanged()
                                    recyclerViewTrack.visibility = View.VISIBLE
                                    progressOfSearch.visibility = View.GONE
                                    placeImgSearchErr.visibility = View.GONE
                                    placeTextError.visibility = View.GONE
                                    btnUpdateSearch.visibility = View.GONE
                                }
                            }
                            if (tracks.isEmpty()) {
                                progressOfSearch.visibility = View.GONE
                                recyclerViewTrack.visibility = View.GONE
                                placeImgSearchErr.visibility = View.VISIBLE
                                placeTextError.visibility = View.VISIBLE
                                btnUpdateSearch.visibility = View.GONE
                                placeTextError.setText(R.string.nothing_found)
                            }
                        } else {
                            progressOfSearch.visibility = View.GONE
                            recyclerViewTrack.visibility = View.GONE
                            placeImgLinkErr.visibility = View.VISIBLE
                            placeTextError.visibility = View.VISIBLE
                            placeTextError.setText(response.code())
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        progressOfSearch.visibility = View.GONE
                        recyclerViewTrack.visibility = View.GONE
                        placeImgLinkErr.visibility = View.VISIBLE
                        placeTextError.visibility = View.VISIBLE
                        btnUpdateSearch.visibility = View.VISIBLE
                        placeTextError.setText(R.string.no_link)
                        btnUpdateSearch.setOnClickListener {
                            recyclerViewTrack.visibility = View.VISIBLE
                            placeImgLinkErr.visibility = View.GONE
                            placeTextError.visibility = View.GONE
                            btnUpdateSearch.visibility = View.GONE
                            findByInput()
                        }
                    }

                })
            }
        }
        searchRunnable = Runnable {findByInput()}

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                findByInput()
            }
            false
        }
        editTextSearch.addTextChangedListener(textWatcherSearch)

    }
    fun searchDebounce(){
        handlerMainThread.removeCallbacks(searchRunnable)
        handlerMainThread.postDelayed(searchRunnable, SEARCH_DELAY)
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
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handlerMainThread.postDelayed({ isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    private fun onClick(track: Track) {
        if (clickDebounce()) {
            val player = Intent(this, PlayerActivity::class.java)
            player.putExtra(Track::class.java.canonicalName, track)
            startActivity(player)
        }
    }


    companion object {
        const val SEARCH_AMOUNT = "SEARCH_AMOUNT"
        const val AMOUNT_DEF = ""
        const val SEARCH_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}