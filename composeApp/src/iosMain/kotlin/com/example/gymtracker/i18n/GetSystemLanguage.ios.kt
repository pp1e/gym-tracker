package com.example.gymtracker.i18n

import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

fun getSystemLanguage(): Language {
    val locale = NSLocale.preferredLanguages.firstOrNull() as? String ?: "en"
    return Language.fromLocale(locale)
}
