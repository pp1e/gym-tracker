package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.gymtracker.components.calendar.CalendarComponent
import com.example.gymtracker.domain.CompletedTrainingShort
import com.example.gymtracker.ui.elements.CalendarLegendAndTrainingInfo
import com.example.gymtracker.ui.elements.TrainingsCalendar

@Composable
fun CalendarScreen(
    component: CalendarComponent,
    paddingValues: PaddingValues,
) {
    val model by component.model.subscribeAsState()
    var selectedCompletedTrainings: List<CompletedTrainingShort> by remember {
        mutableStateOf(emptyList())
    }

    Column(
        modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TrainingsCalendar(
            completedTrainings = model.completedTrainings,
            onDaySelected = { localDate ->
                selectedCompletedTrainings =
                    model.completedTrainings
                        .filter { it.startedAt.date == localDate }
            },
        )

        CalendarLegendAndTrainingInfo(
            completedTrainingTitles = model.completedTrainingTitles,
            selectedCompletedTrainings = selectedCompletedTrainings,
            onTrainingClick = component::onTrainingClicked,
        )
    }
}
