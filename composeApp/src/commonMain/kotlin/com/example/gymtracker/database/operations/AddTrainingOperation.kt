package com.example.gymtracker.database.operations

import com.example.gymtracker.components.entities.TrainingInsert
import com.example.gymtracker.database.awaitMaxId
import database.ApproachQueries
import database.ExerciseQueries
import database.TrainingQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

suspend fun CoroutineScope.executeAddTrainingOperation(
    training: TrainingInsert,
    trainingQueries: TrainingQueries,
    exerciseQueries: ExerciseQueries,
    approachQueries: ApproachQueries,
): Long {
    val trainingId = trainingQueries
        .maxId()
        .awaitMaxId { it.MAX }
    trainingQueries.insert(
        id = trainingId
    )
    training.exercises.map { exercise ->
        launch {
            val exerciseId = exerciseQueries
                .maxId()
                .awaitMaxId { it.MAX }
            exerciseQueries.insert(
                id = exerciseId,
                ordinal = exercise.ordinal.toLong(),
                exercise_template_id = exercise.exerciseTemplateId,
                training_id = trainingId,
            )

            exercise.approaches.map { approach ->
                launch {
                    approachQueries.insert(
                        ordinal = approach.ordinal.toLong(),
                        weight = approach.weight?.toDouble(),
                        repetitions = approach.repetitions.toLong(),
                        exercise_id = exerciseId,
                    )
                }

            }.joinAll()
        }
    }.joinAll()

    return trainingId
}
