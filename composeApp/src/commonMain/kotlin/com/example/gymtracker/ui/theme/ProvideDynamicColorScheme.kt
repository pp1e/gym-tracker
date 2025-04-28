package com.example.gymtracker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
expect fun provideDynamicColorScheme(darkTheme: Boolean): ColorScheme?
