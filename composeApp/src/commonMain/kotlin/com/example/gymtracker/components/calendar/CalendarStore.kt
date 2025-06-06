package com.example.gymtracker.components.calendar

import com.arkivanov.mvikotlin.core.store.Store
import com.example.gymtracker.domain.CompletedTrainingShort
import com.example.gymtracker.domain.CompletedTrainingTitle

internal interface CalendarStore: Store<CalendarStore.Intent, CalendarStore.State, Nothing> {
    sealed class Intent

    data class State(
        val completedTrainings: List<CompletedTrainingShort> = emptyList(),
        val completedTrainingTitles: List<CompletedTrainingTitle> = emptyList(),
    )
}
