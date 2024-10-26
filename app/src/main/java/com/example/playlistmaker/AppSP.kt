package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate


class AppSP: Application() {
    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE)
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

    fun getString(historyTrack: String): String? {
        return sharedPrefs.getString(historyTrack, null)
    }
    fun edit(constHistory:String,json:String){
        sharedPrefs.edit()
            .putString(constHistory, json)
            .apply()
    }
    companion object {
        const val PRACTICUM_EXAMPLE_PREFERENCES = "example_preferences"
        const val SETTINGS_KEY = "settings_theme_key"
    }
}