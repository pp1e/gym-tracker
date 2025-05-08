package com.example.gymtracker.database.queryExecutors

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.CurrentTraining
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.domain.ExerciseTemplate
import com.example.gymtracker.domain.Training
import com.example.gymtracker.utils.safeLet
import database.currentTraining.Get
import kotlinx.datetime.LocalDateTime

suspend fun executeGetCurrentTrainingQuery(getCurrentTrainingQuery: Query<Get>) =
    groupCurrentTrainingEntities(
        currentTraining = getCurrentTrainingQuery.awaitAsList(),
    )
        .firstOrNull()

private fun groupCurrentTrainingEntities(currentTraining: List<Get>) =
    currentTraining
        .groupBy { it.id }
        .map { (_, trainingRows) ->
            trainingRows.firstOrNull()?.let { trainingRow ->
                CurrentTraining(
                    name = trainingRow.name,
                    startedAt =
                        LocalDateTime.parse(
                            trainingRow.started_at,
                        ),
                    training =
                        Training(
                            id = trainingRow.training_id,
                            exercises =
                                trainingRows
                                    .groupBy { it.id_ }
                                    .mapNotNull { (exerciseId, exerciseRows) ->
                                        safeLet(
                                            exerciseId,
                                            exerciseRows.firstOrNull()?.exercise_template_id,
                                            exerciseRows.firstOrNull()?.name_,
                                            exerciseRows.firstOrNull()?.ordinal,
                                        ) { exerciseId, exerciseTemplateId, templateName, ordinal ->
                                            Exercise(
                                                id = exerciseId,
                                                ordinal = ordinal.toInt(),
                                                template =
                                                    ExerciseTemplate(
                                                        id = exerciseTemplateId,
                                                        name = templateName,
                                                        muscleGroup = exerciseRows.firstOrNull()?.name__,
                                                    ),
                                                approaches =
                                                    exerciseRows.mapNotNull {
                                                        safeLet(
                                                            it.id____,
                                                            it.ordinal_,
                                                            it.repetitions,
                                                            it.weight,
                                                        ) { approachId, ordinal, repetitions, weight ->
                                                            Approach(
                                                                id = approachId,
                                                                ordinal = ordinal.toInt(),
                                                                repetitions = repetitions.toInt(),
                                                                weight = weight.toFloat(),
                                                            )
                                                        }
                                                    },
                                            )
                                        }
                                    },
                        ),
                )
            }
        }
