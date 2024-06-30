package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {
var darkTheme = false
    lateinit var sharedPrefs: SharedPreferences
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
    fun saveSwitchDarkTheme(darkThemeEnabled: Boolean){
        if (darkThemeEnabled) {
            sharedPrefs.edit().putBoolean(SETTINGS_KEY,true).apply()
        } else {
            sharedPrefs.edit().remove(SETTINGS_KEY).apply()
        }
        switchTheme(darkThemeEnabled)
    }
    companion object {
    const val PRACTICUM_EXAMPLE_PREFERENCES = "example_preferences"
    const val SETTINGS_KEY = "settings_theme_key"

}
}