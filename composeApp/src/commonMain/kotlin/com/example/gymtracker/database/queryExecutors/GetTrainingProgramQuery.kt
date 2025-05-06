package com.example.gymtracker.database.queryExecutors

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.domain.ExerciseTemplate
import com.example.gymtracker.domain.Training
import com.example.gymtracker.domain.TrainingProgram
import com.example.gymtracker.utils.safeLet
import database.trainingProgram.Get

suspend fun executeGetTrainingProgramQuery(getTrainingProgramQuery: Query<Get>) =
    groupTrainingProgramEntities(
        trainingProgram = getTrainingProgramQuery.awaitAsList(),
    ).firstOrNull()

private fun groupTrainingProgramEntities(trainingProgram: List<Get>) =
    trainingProgram
        .groupBy { it.id }
        .map { (_, trainingRows) ->
            safeLet(
                trainingRows.firstOrNull()?.id,
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
                                                        ) { approachId, ordinal, repetitions ->
                                                            Approach(
                                                                id = approachId,
                                                                ordinal = ordinal.toInt(),
                                                                repetitions = repetitions.toInt(),
                                                                weight = it.weight?.toFloat(),
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
