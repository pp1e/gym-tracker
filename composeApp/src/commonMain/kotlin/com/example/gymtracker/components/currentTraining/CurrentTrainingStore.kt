package com.example.gymtracker.components.currentTraining

import com.arkivanov.mvikotlin.core.store.Store
import com.example.gymtracker.domain.Training

internal interface CurrentTrainingStore : Store<CurrentTrainingStore.Intent, CurrentTrainingStore.State, Nothing> {
    sealed class Intent

    data class State(
        val training: Training? = null
    )
}
