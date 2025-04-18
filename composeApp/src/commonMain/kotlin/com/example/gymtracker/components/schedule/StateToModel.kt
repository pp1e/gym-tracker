package com.example.gymtracker.components.schedule

import com.example.gymtracker.domain.TrainingProgram

internal fun stateToModel(state: ScheduleStore.State) = ScheduleComponent.Model(
    trainingProgram = state.trainingProgram?.let {
        filterTrainingProgram(
            trainingProgram = state.trainingProgram,
            deleteApproachRequest = state.deleteApproachRequests,
            deleteExerciseRequest = state.deleteExerciseRequests,
        )
    },
    trainingProgramsShort = state.trainingProgramsShort.filterNot {
        it.id == state.trainingProgram?.id
    },
    exerciseTemplateNames = state.exerciseTemplates.map { it.name },
    exerciseName = state.exerciseName,
    approachesCount = state.approachesCount,
    repetitionsCount = state.repetitionsCount,
    weight = state.weight,
)

private fun filterTrainingProgram(
    trainingProgram: TrainingProgram,
    deleteApproachRequest: List<Long>,
    deleteExerciseRequest: List<Long>,
) = trainingProgram.copy(
    training = trainingProgram.training.copy(
        exercises = trainingProgram.training.exercises
            .filterNot { it.id in deleteExerciseRequest }
            .map { exercise ->
                exercise.copy(
                    approaches = exercise.approaches.filterNot { it.id in deleteApproachRequest }
                )
            }
    )
)
