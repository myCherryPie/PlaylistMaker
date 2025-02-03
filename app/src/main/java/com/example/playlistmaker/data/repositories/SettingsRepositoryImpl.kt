package com.example.playlistmaker.data.repositories

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.SettingsRepo


class SettingsRepositoryImpl(private val sharedPrefs:SharedPreferences): SettingsRepo {

    private var darkTheme = sharedPrefs.getBoolean(SETTINGS_KEY, false)


    override fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    override fun saveSettingTheme(mode:Boolean) {
        sharedPrefs.edit().putBoolean(SETTINGS_KEY,mode).apply()
    }
    override fun getSettingTheme(): Boolean {
        return sharedPrefs.getBoolean(SETTINGS_KEY,false)
    }

    companion object {
        const val PRACTICUM_EXAMPLE_PREFERENCES = "example_preferences"
        const val SETTINGS_KEY = "settings_theme_key"
    }
}