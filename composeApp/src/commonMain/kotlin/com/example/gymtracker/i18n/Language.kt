package com.example.gymtracker.i18n

import com.example.gymtracker.settings.Settings

enum class Language(
    val strings: Strings,
): Translatable {
    EN(
        strings = EnglishStrings,
    ) {
        override fun translate() = Settings.language.strings.english
    },
    RU(
        strings = RussianStrings
    )
    {
        override fun translate() = Settings.language.strings.russian
    };

    companion object {
        fun fromLocale(locale: String): Language =
            when (locale.lowercase()) {
                "ru", "ru-ru" -> RU
                else -> EN
            }
    }
}
