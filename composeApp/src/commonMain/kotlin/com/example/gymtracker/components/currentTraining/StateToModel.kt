package com.example.gymtracker.components.currentTraining

internal fun stateToModel(state: CurrentTrainingStore.State) =
    CurrentTrainingComponent.Model(
        currentTraining =
            state.currentTraining?.copy(
                training =
                    state.currentTraining.training.filter(
                        deleteApproachIds = state.deleteApproachRequests,
                        deleteExerciseIds = state.deleteExerciseRequests,
                    ),
            ),
        //    trainingProgramsShort = state.trainingProgramsShort.filterNot {
//        it.name == state.currentTraining.
//    },
        trainingProgramsShort = state.trainingProgramsShort,
        exerciseTemplateNames = state.exerciseTemplates.map { it.name },
        exerciseName = state.exerciseName,
        approachesCount = state.approachesCount,
        repetitionsCount = state.repetitionsCount,
        weight = state.weight,
        isTrainingIrrelevant = state.isTrainingIrrelevant,
    )
