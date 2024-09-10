package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppSP: Application() {
    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPrefs  = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(SETTINGS_KEY, false)

    }


    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    fun saveSettingTheme(mode:Boolean) {
        sharedPrefs.edit().putBoolean(SETTINGS_KEY,mode).apply()
    }
    fun getSettingTheme(): Boolean {
        return sharedPrefs.getBoolean(SETTINGS_KEY,false)
    }
    fun saveHistoryOfSearch(tracks : ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit()
            .putString(HISTORY_TRACK, json)
            .apply()
    }

    fun getHistoryOfSearch(): ArrayList<Track> {
        val json = sharedPrefs.getString(HISTORY_TRACK,null)?:return ArrayList()
        val token = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, token)
    }

    companion object {
        const val PRACTICUM_EXAMPLE_PREFERENCES = "example_preferences"
        const val SETTINGS_KEY = "settings_theme_key"
        const val HISTORY_TRACK = "history_track"

    }
}