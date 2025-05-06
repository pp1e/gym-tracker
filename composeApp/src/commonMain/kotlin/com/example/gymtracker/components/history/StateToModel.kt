package com.example.gymtracker.components.history

internal fun stateToModel(state: HistoryStore.State) =
    HistoryComponent.Model(
        completedTrainings = state.completedTrainings,
    )
