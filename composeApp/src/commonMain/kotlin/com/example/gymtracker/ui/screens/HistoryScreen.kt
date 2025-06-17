package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.gymtracker.components.history.HistoryComponent
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.elements.CompletedTrainingEntry
import com.example.gymtracker.ui.elements.translation
import com.example.gymtracker.utils.capitalize
import com.example.gymtracker.utils.now
import kotlinx.datetime.LocalDateTime

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
            items(model.completedTrainingMonths) { completedTrainingMonth ->
                val currentYear = LocalDateTime.now().year

                Text(
                    modifier =
                        Modifier
                            .padding(UiConstants.defaultPadding)
                            .fillParentMaxWidth(),
                    text =
                        completedTrainingMonth.month
                            .translation()
                            .capitalize()
                            .plus(
                                if (completedTrainingMonth.year != currentYear) {
                                    ", ${completedTrainingMonth.year}"
                                } else {
                                    ""
                                },
                            ),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                HorizontalDivider(
                    modifier =
                        Modifier
                            .padding(
                                bottom = UiConstants.defaultPadding,
                            ),
                )

                for (completedTrainingWeek in completedTrainingMonth.completedTrainingWeeks) {
                    Text(
                        modifier =
                            Modifier
                                .fillParentMaxWidth(),
                        text =
                            when (completedTrainingWeek.weekOrdinal) {
                                0 -> I18nManager.strings.currentWeek.capitalize()
                                1 -> I18nManager.strings.lastWeek.capitalize()
                                2, 3, 4 ->
                                    "${completedTrainingWeek.weekOrdinal}" +
                                        " ${I18nManager.strings.weeksAgo}"
                                else ->
                                    "${completedTrainingWeek.weekOrdinal}" +
                                        " ${I18nManager.strings.manyWeeksAgo}"
                            },
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    )

                    for (completedTraining in completedTrainingWeek.completedTrainings)
                        CompletedTrainingEntry(
                            onClicked = component::onTrainingClicked,
                            completedTraining = completedTraining,
                        )
                }
            }
        }
    }
}
