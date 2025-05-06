package com.example.gymtracker.components.schedule

internal fun stateToModel(state: ScheduleStore.State) =
    ScheduleComponent.Model(
        trainingProgram =
            state.trainingProgram?.copy(
                training =
                    state.trainingProgram.training.filter(
                        deleteApproachIds = state.deleteApproachRequests,
                        deleteExerciseIds = state.deleteExerciseRequests,
                    ),
            ),
        trainingProgramsShort =
            state.trainingProgramsShort.filterNot {
                it.id == state.trainingProgram?.id
            },
        exerciseTemplateNames = state.exerciseTemplates.map { it.name },
        exerciseName = state.exerciseName,
        approachesCount = state.approachesCount,
        repetitionsCount = state.repetitionsCount,
        weight = state.weight,
    )
