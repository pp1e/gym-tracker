package com.example.gymtracker.database.queryExecutors

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.CompletedTraining
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.domain.ExerciseTemplate
import com.example.gymtracker.domain.Training
import com.example.gymtracker.utils.safeLet
import database.GetById
import kotlinx.datetime.LocalDateTime

suspend fun getCompletedTrainingQuery(getCompletedTrainingQuery: Query<GetById>) =
    groupCompletedTrainingEntities(
        currentTraining = getCompletedTrainingQuery.awaitAsList(),
    ).firstOrNull()

private fun groupCompletedTrainingEntities(currentTraining: List<GetById>) =
    currentTraining
        .groupBy { it.id }
        .map { (_, trainingRows) ->
            trainingRows.firstOrNull()?.let { trainingRow ->
                CompletedTraining(
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
