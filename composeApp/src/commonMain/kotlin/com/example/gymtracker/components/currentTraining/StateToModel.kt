package com.example.gymtracker.components.currentTraining

internal fun stateToModel(state: CurrentTrainingStore.State) = CurrentTrainingComponent.Model(
    training = state.training,
)
