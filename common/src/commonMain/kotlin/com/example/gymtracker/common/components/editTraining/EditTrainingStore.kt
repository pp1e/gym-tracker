package com.example.gymtracker.common.components.editTraining

import com.arkivanov.mvikotlin.core.store.Store

internal interface EditTrainingStore : Store<EditTrainingStore.Intent, EditTrainingStore.State, Nothing> {
    sealed class Intent

    object State
}
