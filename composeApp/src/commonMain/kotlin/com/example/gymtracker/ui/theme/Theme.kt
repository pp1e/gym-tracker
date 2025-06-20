package com.example.gymtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.gymtracker.i18n.Language.EN
import com.example.gymtracker.i18n.Language.RU
import com.example.gymtracker.i18n.Translatable
import com.example.gymtracker.settings.Settings

enum class AppTheme: Translatable {
    LIGHT {
        override fun translate() = Settings.language.strings.light
    }, DARK {
        override fun translate() = Settings.language.strings.dark
    }, SYSTEM {
        override fun translate() = Settings.language.strings.system
    };

    companion object {
        fun parseFromString(
            string: String?,
        ): AppTheme = string?.let {
            try {
                AppTheme.valueOf(string)
            } catch (e: IllegalArgumentException) {
                SYSTEM
            }
        } ?: SYSTEM
    }
}

private val DarkColorScheme =
    darkColorScheme(
        primary = LightBlue,
        onPrimary = LightGray,
        secondary = GreyBlue,
        onSecondary = Color.Black,
        tertiary = DarkGreyBlue,
        onTertiary = Color.White,
        background = DeepBlack,
        onBackground = LightGray,
        surface = Color(0xFF1E1E1E), // Темно-серый для карточек, всплывающих меню и других поверхностей
        onSurface = Color(0xFFB9C7CE), // Серо-голубой текст на темно-сером фоне
    )

private val LightColorScheme =
    lightColorScheme(
        primary = LightBlue,
        onPrimary = Color.White,
        secondary = GreyBlue,
        onSecondary = Color.Black,
        tertiary = DarkGreyBlue,
        onTertiary = Color.White,
        background = Color(0xFFF5F5F5), // Светло-серый для основного фона
        onBackground = DeepBlack,
        surface = Color.White,
        onSurface = Color(0xFF37474F), // Темно-серый текст на белом фоне
    )

@Composable
fun GymTrackerTheme(
    theme: AppTheme,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val darkTheme = when(theme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM ->  isSystemInDarkTheme()
    }

    val colorScheme =
        when {
            dynamicColor -> provideDynamicColorScheme(darkTheme) ?: defaultColorScheme(darkTheme)
            else -> defaultColorScheme(darkTheme)
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

private fun defaultColorScheme(darkTheme: Boolean) = if (darkTheme) DarkColorScheme else LightColorScheme
