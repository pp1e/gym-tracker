package com.example.gymtracker.i18n

import android.content.res.Resources

actual fun getSystemLanguage(): Language {
    val locale = Resources.getSystem().configuration.locales.get(0).language
    return Language.fromLocale(locale)
}
