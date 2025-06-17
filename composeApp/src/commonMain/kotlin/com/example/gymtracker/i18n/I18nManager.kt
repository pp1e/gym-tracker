package com.example.gymtracker.i18n

enum class Language {
    EN, RU;

    companion object {
        fun fromLocale(locale: String): Language =
            when (locale.lowercase()) {
                "ru", "ru-ru" -> RU
                else -> EN
            }
    }
}

object I18nManager {
    private lateinit var _currentLanguage: Language
    private lateinit var _strings: Strings

    val currentLanguage: Language get() = _currentLanguage
    val strings: Strings get() = _strings

    init {
        setLanguage(getSystemLanguage())
    }

    fun setLanguage(lang: Language) {
        _currentLanguage = lang
        _strings = when (lang) {
            Language.EN -> EnglishStrings
            Language.RU -> RussianStrings
        }
    }
}
