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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.gymtracker.components.history.HistoryComponent
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.components.CompletedTrainingEntry
import com.example.gymtracker.ui.components.SubtitleText

@Composable
fun HistoryScreen(
    paddingValues: PaddingValues,
    component: HistoryComponent,
) {
    val model by component.model.subscribeAsState()

    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize(),
    ) {
        Column( // TODO lazy column
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION)
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SubtitleText(
                text = "Эта неделя",
            )

            CompletedTrainingEntry(
                onClicked = component::onTrainingClicked,
            )

            CompletedTrainingEntry(
                onClicked = component::onTrainingClicked,
            )

            CompletedTrainingEntry(
                onClicked = component::onTrainingClicked,
            )

            SubtitleText(
                text = "Прошлая неделя",
            )

            CompletedTrainingEntry(
                onClicked = component::onTrainingClicked,
            )

            SubtitleText(
                text = "Две недели назад",
            )

            CompletedTrainingEntry(
                onClicked = component::onTrainingClicked,
            )

            CompletedTrainingEntry(
                onClicked = component::onTrainingClicked,
            )
        }
    }
}
