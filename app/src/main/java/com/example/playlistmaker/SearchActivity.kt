package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
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

        if (savedInstanceState != null) {
            countValue = savedInstanceState.getString(SEARCH_AMOUNT, AMOUNT_DEF)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
                    }

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

    private val retrofit = Retrofit.Builder()
        .baseUrl(getString(R.string.base_url_itunes))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val playListService = retrofit.create(ItunseApi::class.java)
    var tracks = mutableListOf<Track>()
    val tracksAdapter = TrackAdapter(tracks)
    var placeholderTextError: TextView = findViewById<TextView>(R.id.text_error_search)
    var placeholderImageErorr: ImageView = findViewById<ImageView>(R.id.image_error_search)
    val editTextSearch: EditText = findViewById<EditText>(R.id.edit_text_search)
    private fun search() {

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
                        }
                        if (tracks.isEmpty()) {
                            showError(getString(R.string.nothing_found), "")
                        } else {
                            showError("", "")
                        }
                    } else {
                        showError(getString(R.string.no_link), response.code().toString())
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
                fun showError(text: String, additionalMessage: String) {
                    if (text.isNotEmpty()) {
                        placeholderImageErorr.visibility = View.VISIBLE
                        placeholderTextError.visibility = View.VISIBLE
                        tracks.clear()
                        tracksAdapter.notifyDataSetChanged()
                        placeholderImageErorr
                        placeholderTextError.text = text
                        if (additionalMessage.isNotEmpty()) {
                            Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        placeholderTextError.visibility = View.GONE
                    }
                }
            })
        }
    }

    companion object {
        const val SEARCH_AMOUNT = "SEARCH_AMOUNT"
        const val AMOUNT_DEF = ""
    }
}

