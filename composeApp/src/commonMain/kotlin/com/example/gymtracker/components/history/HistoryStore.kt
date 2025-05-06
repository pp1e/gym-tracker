package com.example.gymtracker.components.history

import com.arkivanov.mvikotlin.core.store.Store
import com.example.gymtracker.domain.CompletedTrainingShort

internal interface HistoryStore : Store<HistoryStore.Intent, HistoryStore.State, Nothing> {
    sealed class Intent

    data class State(
        val completedTrainings: List<CompletedTrainingShort> = emptyList(),
    )
}
