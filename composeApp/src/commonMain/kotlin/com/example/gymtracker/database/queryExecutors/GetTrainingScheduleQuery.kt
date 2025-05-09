package com.example.gymtracker.database.queryExecutors

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.domain.ExerciseTemplate
import com.example.gymtracker.domain.Training
import com.example.gymtracker.domain.TrainingProgram
import com.example.gymtracker.utils.safeLet
import database.trainingSchedule.Get

suspend fun executeGetTrainingScheduleQuery(getTrainingScheduleQuery: Query<Get>) =
    groupTrainingScheduleEntities(
        trainingSchedule = getTrainingScheduleQuery.awaitAsList(),
    ).firstOrNull()

private fun groupTrainingScheduleEntities(trainingSchedule: List<Get>) =
    trainingSchedule
        .groupBy { it.id }
        .map { (_, trainingRows) ->
            safeLet(
                trainingRows.firstOrNull()?.id_,
                trainingRows.firstOrNull()?.name,
                trainingRows.firstOrNull()?.training_id,
            ) { trainingProgramId, trainingProgramName, trainingId ->
                TrainingProgram(
                    id = trainingProgramId,
                    name = trainingProgramName,
                    training =
                        Training(
                            id = trainingId,
                            exercises =
                                trainingRows
                                    .groupBy { it.id__ }
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
                                                            it.id_____,
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
