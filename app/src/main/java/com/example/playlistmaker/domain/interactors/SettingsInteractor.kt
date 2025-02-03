package com.example.playlistmaker.domain.interactors

interface SettingsInteractor {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun saveSettingTheme(mode:Boolean)
    fun getSettingTheme(): Boolean
}