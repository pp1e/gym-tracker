package com.example.gymtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
    darkColorScheme(
//    primary = Color(0xFF64B5F6), // Нежный голубой для акцентных элементов
        primary = LightBlue,
//    onPrimary = Color.Black, // Черный текст или иконки на голубом фоне
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

//private val LightColorScheme =
//    lightColorScheme(
//        primary = Purple40,
//        secondary = PurpleGrey40,
//        tertiary = Pink40,
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//     */
//    )

@Composable
fun GymTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor -> provideDynamicColorScheme(darkTheme) ?: defaultColorScheme(darkTheme)
        else -> defaultColorScheme(darkTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

private fun defaultColorScheme(darkTheme: Boolean) =
    if (darkTheme) DarkColorScheme else LightColorScheme
