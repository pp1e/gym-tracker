package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.gymtracker.components.history.HistoryComponent
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.elements.CompletedTrainingEntry

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
        LazyColumn(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(model.completedTrainings) { completedTraining ->
                CompletedTrainingEntry(
                    onClicked = component::onTrainingClicked,
                    completedTraining = completedTraining,
                )
            }

//            SubtitleText(
//                text = "Эта неделя",
//            )
//
//            CompletedTrainingEntry(
//                onClicked = component::onTrainingClicked,
//            )
//
//            CompletedTrainingEntry(
//                onClicked = component::onTrainingClicked,
//            )
//
//            CompletedTrainingEntry(
//                onClicked = component::onTrainingClicked,
//            )
//
//            SubtitleText(
//                text = "Прошлая неделя",
//            )
//
//            CompletedTrainingEntry(
//                onClicked = component::onTrainingClicked,
//            )
//
//            SubtitleText(
//                text = "Две недели назад",
//            )
//
//            CompletedTrainingEntry(
//                onClicked = component::onTrainingClicked,
//            )
//
//            CompletedTrainingEntry(
//                onClicked = component::onTrainingClicked,
//            )
        }
    }
}
