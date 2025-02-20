package com.example.gymtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
//    primary = Color(0xFF64B5F6), // Нежный голубой для акцентных элементов
    primary = Color(0xFF4E8DC3),
//    onPrimary = Color.Black, // Черный текст или иконки на голубом фоне
    onPrimary = Color(0xFFE0E0E0),

    secondary = Color(0xFF90A4AE), // Серый с легким синим оттенком для второстепенных акцентов
    onSecondary = Color.Black, // Черный текст на сером фоне

    tertiary = Color(0xFF546E7A), // Темно-синий/серый для дополнительных акцентов
    onTertiary = Color.White, // Белый текст на темно-сером фоне

    background = Color(0xFF121212), // Глубокий черный для основного фона
    onBackground = Color(0xFFE0E0E0), // Светло-серый текст на черном фоне

    surface = Color(0xFF1E1E1E), // Темно-серый для карточек, всплывающих меню и других поверхностей
    onSurface = Color(0xFFB9C7CE), // Серо-голубой текст на темно-сером фоне
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun GymTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
