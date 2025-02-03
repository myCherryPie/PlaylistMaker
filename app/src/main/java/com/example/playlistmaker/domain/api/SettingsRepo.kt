package com.example.playlistmaker.domain.api

interface SettingsRepo {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun saveSettingTheme(mode:Boolean)
    fun getSettingTheme(): Boolean
}