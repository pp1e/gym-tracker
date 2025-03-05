package com.example.gymtracker.components.currentTraining

import com.arkivanov.mvikotlin.core.store.Store

internal interface CurrentTrainingStore : Store<CurrentTrainingStore.Intent, CurrentTrainingStore.State, Nothing> {
    sealed class Intent

    object State
}
