package com.example.gymtracker.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.utils.toDp

object UiConstants {
    // Numerical constants
    const val ANIMATION_DEFAULT_DURATION_MILLIS = 300L
    const val COMMON_WIDTH_FRACTION = 0.9f
    const val FAB_ADD_WEIGHT = 0.75f

    // Default sizes
    val defaultPadding = 8.dp
    val topAppBarHorizontalPadding = 16.dp
    val defaultFontSize = 20.sp

    // Weekday Switcher
    val WeekdaySwitcherPaddingValues =
        PaddingValues(
            bottom = 10.dp,
        )
    val WeekdaySwitcherDaySize = 48.dp
    val WeekdaySwitcherFullHeight =
        WeekdaySwitcherPaddingValues.calculateTopPadding() +
                WeekdaySwitcherPaddingValues.calculateBottomPadding() +
                WeekdaySwitcherDaySize

    // Floating Action Buttons
    val FABHeight = 56.dp
    val FABPanelPadding =
        PaddingValues(
            vertical = 15.dp,
            horizontal = 20.dp,
        )

    // Functions
    @Composable
    fun calculateNumberInputHeight() = defaultFontSize.toDp() + 32.dp

    @Composable
    fun getApproachFontColor() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
}
