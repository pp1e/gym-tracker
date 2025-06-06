package com.example.gymtracker.components.calendar

internal fun stateToModel(state: CalendarStore.State) =
    CalendarComponent.Model(
        completedTrainings = state.completedTrainings,
        completedTrainingTitles = state.completedTrainingTitles,
    )
