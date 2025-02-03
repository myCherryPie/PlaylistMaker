package com.example.playlistmaker.domain.interactors

import com.example.playlistmaker.domain.api.SettingsRepo

class SettingsInteractorImpl(private val repo: SettingsRepo): SettingsInteractor {
    override fun switchTheme(darkThemeEnabled: Boolean) {
        repo.switchTheme(darkThemeEnabled)
    }

    override fun saveSettingTheme(mode: Boolean) {
        repo.saveSettingTheme(mode)
    }

    override fun getSettingTheme(): Boolean {
       return repo.getSettingTheme()
    }

}