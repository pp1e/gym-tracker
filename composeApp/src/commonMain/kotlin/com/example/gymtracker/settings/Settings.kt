package com.example.gymtracker.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gymtracker.i18n.Language
import com.example.gymtracker.i18n.getSystemLanguage
import com.example.gymtracker.ui.theme.AppTheme

object Settings {
    private const val LANGUAGE_KEY = "language"
    private const val THEME_KEY = "theme"

    private var _language by mutableStateOf(
        SettingsStorage
            .getString(LANGUAGE_KEY)
            ?.let(Language::valueOf)
            ?: getSystemLanguage()
    )
    private var _theme by mutableStateOf(
        AppTheme.parseFromString(
            string = SettingsStorage
                .getString(THEME_KEY)
        )
    )

    val language: Language get() = _language
    val theme: AppTheme get() = _theme

    fun setLanguage(
        language: Language,
    ) {
        SettingsStorage.putString(
            key = LANGUAGE_KEY,
            value = language.name,
        )
        _language = language
    }

    fun setTheme(
        theme: AppTheme,
    ) {

        SettingsStorage.putString(
            key = THEME_KEY,
            value = theme.name,
        )
        _theme = theme
    }
}
