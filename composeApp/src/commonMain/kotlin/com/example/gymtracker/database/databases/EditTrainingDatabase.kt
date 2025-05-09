package com.example.gymtracker.database.databases

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.zip
import com.example.gymtracker.database.execute
import com.example.gymtracker.database.observe
import com.example.gymtracker.database.operations.executeAddExerciseOperation
import com.example.gymtracker.database.queryExecutors.executeGetExerciseTemplateListQuery
import com.example.gymtracker.database.queryExecutors.getCompletedTrainingQuery
import database.ApproachQueries
import database.CompletedTrainingQueries
import database.ExerciseQueries
import database.ExerciseTemplateQueries

class EditTrainingDatabase(
    private val completedTrainingQueries: Single<CompletedTrainingQueries>,
    private val exerciseQueries: Single<ExerciseQueries>,
    private val approachQueries: Single<ApproachQueries>,
    private val exerciseTemplateQueries: Single<ExerciseTemplateQueries>,
) {
    fun observeCompletedTraining(id: Long) =
        completedTrainingQueries
            .map { it.getById(id) }
            .observe(::getCompletedTrainingQuery)

    fun observeExerciseTemplateList() =
        exerciseTemplateQueries
            .map(ExerciseTemplateQueries::getList)
            .observe(::executeGetExerciseTemplateListQuery)

    fun addExercise(
        trainingId: Long,
        exerciseTemplate: NewOrExistingExerciseTemplate,
        approachesCount: Int,
        repetitionsCount: Int,
        weight: Float,
    ) = zip(
        exerciseQueries,
        approachQueries,
        exerciseTemplateQueries,
    ) { exerciseQueries, approachQueries, exerciseTemplateQueries ->
        Triple(exerciseQueries, approachQueries, exerciseTemplateQueries)
    }.execute { (exerciseQueries, approachQueries, exerciseTemplateQueries) ->
        executeAddExerciseOperation(
            trainingId = trainingId,
            exerciseTemplate = exerciseTemplate,
            approachesCount = approachesCount,
            repetitionsCount = repetitionsCount,
            weight = weight,
            exerciseQueries = exerciseQueries,
            exerciseTemplateQueries = exerciseTemplateQueries,
            approachQueries = approachQueries,
            coroutineScope = this,
        )
    }

    fun addApproach(exerciseId: Long) =
        approachQueries
            .execute {
                it.insertSingle(
                    exercise_id = exerciseId,
                    weight = 0.0,
                    repetitions = 5,
                )
            }

    fun updateRepetitions(
        approachId: Long,
        repetitions: Int,
    ) = approachQueries
        .execute {
            it.updateRepetitions(
                id = approachId,
                repetitions = repetitions.toLong(),
            )
        }

    fun updateWeight(
        approachId: Long,
        weight: Float,
    ) = approachQueries
        .execute {
            it.updateWeight(
                id = approachId,
                weight = weight.toDouble(),
            )
        }

    fun updateCompletedTrainingName(name: String) =
        completedTrainingQueries
            .execute {
                it.updateName(name)
            }

    fun deleteApproach(approachId: Long) =
        approachQueries
            .execute {
                it.delete(approachId)
            }

    fun deleteExercise(exerciseId: Long) =
        exerciseQueries
            .execute {
                it.delete(exerciseId)
            }
}
