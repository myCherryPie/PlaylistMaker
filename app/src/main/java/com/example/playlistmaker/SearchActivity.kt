package com.example.playlistmaker

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val tracks = mutableListOf<Track>()
        val tracksAdapter = TrackAdapter(tracks)

       val urlItunesApi = getString(R.string.base_url_itunes)

        val retrofit = Retrofit.Builder()
            .baseUrl(urlItunesApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val playListService = retrofit.create(ItunseApi::class.java)
        val placeTextError: TextView = findViewById<TextView>(R.id.text_error_search)
        val placeImageErorr = findViewById<ImageView>(R.id.image_error_search)
        val editTextSearch = findViewById<EditText>(R.id.edit_text_search)
        val btnUpdateSearch = findViewById<Button>(R.id.btn_update_search)

        if (savedInstanceState != null) {
            countValue = savedInstanceState.getString(SEARCH_AMOUNT, AMOUNT_DEF)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val arrowBackFromSearch = findViewById<ImageButton>(R.id.arrow_back_from_search)
        arrowBackFromSearch.setOnClickListener {
            finish()
        }

        val btnCrossClear = findViewById<ImageView>(R.id.clear_cross_search)
        btnCrossClear.setOnClickListener {
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()
            editTextSearch.setText("")
            editTextSearch.clearFocus()
            editTextSearch.isCursorVisible = false
            btnUpdateSearch.visibility = View.GONE
            placeImageErorr.visibility = View.GONE
            placeTextError.visibility = View.GONE
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


        val recyclerViewTrack = findViewById<RecyclerView>(R.id.recyclerTracks)
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)
        recyclerViewTrack.adapter = tracksAdapter

         fun search() {
            if (editTextSearch.text.isNotEmpty()) {
                playListService.search(editTextSearch.text.toString()).enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                tracksAdapter.notifyDataSetChanged()
                                placeImageErorr.visibility = View.GONE
                                placeTextError.visibility = View.GONE
                                btnUpdateSearch.visibility = View.GONE
                            }
                            if (tracks.isEmpty()) {
                                placeImageErorr.visibility = View.VISIBLE
                                placeTextError.visibility = View.VISIBLE
                                btnUpdateSearch.visibility = View.GONE
                                placeImageErorr.setImageResource(R.drawable.light_nothing_found)
                                placeTextError.setText(R.string.nothing_found)

                            }
                        } else {
                            placeImageErorr.visibility = View.VISIBLE
                            placeTextError.visibility = View.VISIBLE
                            btnUpdateSearch.visibility = View.VISIBLE
                            placeImageErorr.setImageResource(R.drawable.light_no_link)
                            placeTextError.setText(R.string.no_link)
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        placeImageErorr.visibility = View.VISIBLE
                        placeTextError.visibility = View.VISIBLE
                        btnUpdateSearch.visibility = View.VISIBLE
                        placeImageErorr.setImageResource(R.drawable.light_no_link)
                        placeTextError.setText(R.string.no_link)
                    }

                })
            }
        }
        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        editTextSearch.addTextChangedListener(textWatcherSearch)

        btnUpdateSearch.setOnClickListener {
            search()
        }
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

