package com.example.gymtracker.database.operations

import com.example.gymtracker.database.queryExecutors.executeGetExercisesQuery
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.domain.ExerciseShort
import database.ExerciseQueries

suspend fun executeSwapExerciseOrdinalsOperation(
    exerciseFrom: Exercise,
    exerciseTo: Exercise,
    trainingId: Long,
    exerciseQueries: ExerciseQueries,
) = exerciseQueries.transaction {
    val exercises = executeGetExercisesQuery(
        exerciseQueries.get(
            training_id = trainingId,
        )
    )
    val exercisesToUpdate: List<ExerciseShort>
    val newExerciseFromOrdinal: Int

    if (exerciseFrom.ordinal < exerciseTo.ordinal) {
        newExerciseFromOrdinal = exerciseTo.ordinal + 1
        exercisesToUpdate = exercises.filter {
            it.ordinal > exerciseTo.ordinal
        }
            .sortedByDescending { it.ordinal }
    } else {
        newExerciseFromOrdinal = exerciseTo.ordinal
        exercisesToUpdate = exercises.filter {
            it.ordinal >= exerciseTo.ordinal
        }
            .sortedByDescending { it.ordinal }
    }

    for (exercise in exercisesToUpdate) {
        exerciseQueries.increaseOrdinal(exercise.id)
    }
    exerciseQueries.updateOrdinal(
        ordinal = newExerciseFromOrdinal.toLong(),
        id = exerciseFrom.id,
    )
}
