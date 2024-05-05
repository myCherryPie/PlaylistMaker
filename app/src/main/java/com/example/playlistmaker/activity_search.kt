package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val editTextSearch = findViewById<EditText>(R.id.edit_text_search)

        if (savedInstanceState != null) {
            editTextSearch.setText(safeStringSearch).toString()
        }

        val arrowBackFromSearch = findViewById<ImageButton>(R.id.arrow_back_from_search)
        arrowBackFromSearch.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }
        val btnCrossClear = findViewById<ImageView>(R.id.clear_cross_search)
        btnCrossClear.setOnClickListener {
            editTextSearch.setText("")
            inputHideManager(editTextSearch)
            editTextSearch.clearFocus()
            editTextSearch.isCursorVisible = false
        }

        val textWatcherSearch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                editTextSearch.isCursorVisible = true
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnCrossClear.visibility = visibilityClearButton(s)
                countStringNow = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        editTextSearch.addTextChangedListener(textWatcherSearch)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(countStringNow, safeStringSearch)
    }

    companion object {
        var safeStringSearch = ""
        var countStringNow = ""
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        safeStringSearch = savedInstanceState.getString(countStringNow).toString()

    }

    private fun visibilityClearButton(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun inputHideManager(editText: EditText) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}
