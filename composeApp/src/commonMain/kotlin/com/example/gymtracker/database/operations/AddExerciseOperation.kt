package com.example.gymtracker.database.operations

import app.cash.sqldelight.async.coroutines.awaitAsOne
import com.example.gymtracker.database.awaitMaxId
import com.example.gymtracker.database.databases.NewOrExistingExerciseTemplate
import database.ApproachQueries
import database.ExerciseQueries
import database.ExerciseTemplateQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

suspend fun executeAddExerciseOperation(
    trainingId: Long,
    exerciseTemplate: NewOrExistingExerciseTemplate,
    approachesCount: Int,
    repetitionsCount: Int,
    weight: Float,
    exerciseQueries: ExerciseQueries,
    exerciseTemplateQueries: ExerciseTemplateQueries,
    approachQueries: ApproachQueries,
    coroutineScope: CoroutineScope,
) {
    val exerciseId =
        exerciseQueries
            .maxId()
            .awaitMaxId { it.MAX }
    exerciseQueries.insertSingle(
        id = exerciseId,
        training_id = trainingId,
        exercise_template_id =
            when (exerciseTemplate) {
                is NewOrExistingExerciseTemplate.ExistingExerciseTemplate -> exerciseTemplate.id
                is NewOrExistingExerciseTemplate.NewExerciseTemplate -> {
                    exerciseTemplateQueries.insert(
                        name = exerciseTemplate.name,
                        muscle_group_id = null,
                    )
                    exerciseTemplateQueries
                        .getId(exerciseTemplate.name)
                        .awaitAsOne()
                }
            },
    )
    var ordinal = 1L
    List(approachesCount) {
        coroutineScope.launch {
            approachQueries
                .insert(
                    ordinal = ordinal++,
                    exercise_id = exerciseId,
                    repetitions = repetitionsCount.toLong(),
                    weight = weight.toDouble(),
                )
        }
    }.joinAll()
}
