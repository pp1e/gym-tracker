package com.example.gymtracker.i18n

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

    fun translation() = when(this) {
        EN -> I18nManager.strings.english
        RU -> I18nManager.strings.russian
    }
}
