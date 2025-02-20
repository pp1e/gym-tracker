package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.components.CompletedTrainingEntry
import com.example.gymtracker.ui.components.SubtitleText

private const val WIDTH_COEF = 0.9f

@Composable
fun HistoryScreen(
    paddingValues: PaddingValues,
) {
    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column( // TODO lazy column
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .fillMaxWidth(WIDTH_COEF)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SubtitleText(
                text = "Эта неделя"
            )

            CompletedTrainingEntry()

            CompletedTrainingEntry()

            CompletedTrainingEntry()

            SubtitleText(
                text = "Прошлая неделя"
            )

            CompletedTrainingEntry()

            SubtitleText(
                text = "Две недели назад"
            )

            CompletedTrainingEntry()

            CompletedTrainingEntry()
        }
    }
}