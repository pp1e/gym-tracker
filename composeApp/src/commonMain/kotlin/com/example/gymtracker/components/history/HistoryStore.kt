package com.example.gymtracker.components.history

import com.arkivanov.mvikotlin.core.store.Store
import com.example.gymtracker.domain.CompletedTrainingShort
import kotlinx.datetime.Month

data class CompletedTrainingWeek(
    val weekOrdinal: Int,
    val completedTrainings: List<CompletedTrainingShort>,
)

data class CompletedTrainingMonth(
    val year: Int,
    val month: Month,
    val completedTrainingWeeks: List<CompletedTrainingWeek>,
)

internal interface HistoryStore : Store<HistoryStore.Intent, HistoryStore.State, Nothing> {
    sealed class Intent

    data class State(
        val completedTrainingMonths: List<CompletedTrainingMonth> = emptyList(),
    )
}
