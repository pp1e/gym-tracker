package com.example.gymtracker.database.queryExecutors

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.domain.ExerciseTemplate
import com.example.gymtracker.domain.Training
import com.example.gymtracker.utils.safeLet
import database.currentTraining.Get

suspend fun executeGetCurrentTrainingQuery(
    getCurrentTrainingQuery: Query<Get>,
) =
    groupCurrentTrainingEntities(
        currentTraining = getCurrentTrainingQuery.awaitAsList()
    )
        .firstOrNull()

private fun groupCurrentTrainingEntities(currentTraining: List<Get>) =
    currentTraining
        .groupBy { it.id }
        .map { (_, trainingRows) ->
            trainingRows.firstOrNull()?.training_id?.let { trainingId ->
                Training(
                    id = trainingId,
                    exercises = trainingRows
                        .groupBy { it.id_ }
                        .mapNotNull { (exerciseId, exerciseRows) ->
                            safeLet(
                                exerciseId,
                                exerciseRows.firstOrNull()?.exercise_template_id,
                                exerciseRows.firstOrNull()?.name,
                                exerciseRows.firstOrNull()?.name_,
                                exerciseRows.firstOrNull()?.ordinal,
                            ) { exerciseId, exerciseTemplateId, templateName, muscleGroup, ordinal ->
                                Exercise(
                                    id = exerciseId,
                                    ordinal = ordinal.toInt(),
                                    template = ExerciseTemplate(
                                        id = exerciseTemplateId,
                                        name = templateName,
                                        muscleGroup = muscleGroup,
                                    ),
                                    approaches = exerciseRows.mapNotNull {
                                        safeLet(
                                            it.id____,
                                            it.ordinal_,
                                            it.repetitions,
                                        ) { approachId, ordinal, repetitions ->
                                            Approach(
                                                id = approachId,
                                                ordinal = ordinal.toInt(),
                                                repetitions = repetitions.toInt(),
                                                weight = it.weight?.toFloat(),
                                            )
                                        }
                                    }
                                )
                            }
                        }
                )
            }
        }