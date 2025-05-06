package com.example.gymtracker.components.editTraining

internal fun stateToModel(state: EditTrainingStore.State) =
    EditTrainingComponent.Model(
        completedTraining = state.completedTraining?.copy(
            training =
                state.completedTraining.training.filter(
                    deleteApproachIds = state.deleteApproachRequests,
                    deleteExerciseIds = state.deleteExerciseRequests,
                ),
        ),
        exerciseTemplateNames = state.exerciseTemplates.map { it.name },
        exerciseName = state.exerciseName,
        approachesCount = state.approachesCount,
        repetitionsCount = state.repetitionsCount,
        weight = state.weight,
    )
