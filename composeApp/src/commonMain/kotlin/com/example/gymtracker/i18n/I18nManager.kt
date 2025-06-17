package com.example.gymtracker.i18n

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class Language {
    EN,
    RU,
    ;

    companion object {
        fun fromLocale(locale: String): Language =
            when (locale.lowercase()) {
                "ru", "ru-ru" -> RU
                else -> EN
            }
    }
}

object I18nManager {
    private var _currentLanguage by mutableStateOf(getSystemLanguage())
    private var _strings by mutableStateOf(
        when (_currentLanguage) {
            Language.EN -> {
                EnglishStrings
            }
            Language.RU -> RussianStrings
        }
    )

    val currentLanguage: Language get() = _currentLanguage
    val strings: Strings get() = _strings

    fun setLanguage(lang: Language) {
        _currentLanguage = lang
        _strings =
            when (lang) {
                Language.EN -> {
                    EnglishStrings
                }
                Language.RU -> RussianStrings
            }
    }
}
