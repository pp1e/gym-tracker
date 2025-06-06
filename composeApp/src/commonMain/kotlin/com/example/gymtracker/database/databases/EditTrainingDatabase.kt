package com.example.gymtracker.database.databases

import app.cash.sqldelight.async.coroutines.awaitAsOne
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.example.gymtracker.database.Database
import com.example.gymtracker.database.utils.execute
import com.example.gymtracker.database.utils.observe
import com.example.gymtracker.database.operations.executeAddExerciseOperation
import com.example.gymtracker.database.operations.executeDeleteCompletedTrainingTitleOperation
import com.example.gymtracker.database.operations.executeGetOrInsertCompletedTrainingTitleOperation
import com.example.gymtracker.database.operations.executeSwapApproachOrdinalsOperation
import com.example.gymtracker.database.operations.executeSwapExerciseOrdinalsOperation
import com.example.gymtracker.database.queryExecutors.executeGetExerciseTemplateListQuery
import com.example.gymtracker.database.queryExecutors.getCompletedTrainingQuery
import com.example.gymtracker.database.utils.zipAndExecute
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.Exercise
import database.ApproachQueries
import database.CompletedTrainingQueries
import database.CompletedTrainingTitleQueries
import database.ExerciseQueries
import database.ExerciseTemplateQueries
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

class EditTrainingDatabase(
    private val completedTrainingQueries: Single<CompletedTrainingQueries>,
    private val exerciseQueries: Single<ExerciseQueries>,
    private val approachQueries: Single<ApproachQueries>,
    private val exerciseTemplateQueries: Single<ExerciseTemplateQueries>,
    private val completedTrainingTitleQueries: Single<CompletedTrainingTitleQueries>,
    private val database: Single<Database>,
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
    ) = zipAndExecute(
        exerciseQueries,
        approachQueries,
        exerciseTemplateQueries,
    ) { exerciseQueries, approachQueries, exerciseTemplateQueries ->
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

    fun updateCompletedTrainingName(
        completedTrainingId: Long,
        name: String
    ) = zipAndExecute(
        completedTrainingQueries,
        completedTrainingTitleQueries,
        database,
    ) { completedTrainingQueries, completedTrainingTitleQueries, database ->
        database.transaction {
            val oldTitleId = completedTrainingQueries
                .getTitleId(completedTrainingId)
                .awaitAsOne()

            val newTitleId = executeGetOrInsertCompletedTrainingTitleOperation(
                trainingName = name,
                completedTrainingTitleQueries = completedTrainingTitleQueries,
            )

            completedTrainingQueries.updateTitleId(
                title_id = newTitleId,
                id = completedTrainingId,
            )

            executeDeleteCompletedTrainingTitleOperation(
                completedTrainingTitleQueries = completedTrainingTitleQueries,
                completedTrainingTitleId = oldTitleId,
            )
        }
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

    fun deleteTraining(
        completedTrainingId: Long,
    ) = zipAndExecute(
        completedTrainingQueries,
        completedTrainingTitleQueries,
        database,
    ) { completedTrainingQueries, completedTrainingTitleQueries, database ->
        database.transaction {
            val titleId = completedTrainingQueries
                .getTitleId(completedTrainingId)
                .awaitAsOne()
            completedTrainingQueries.delete(completedTrainingId)
            executeDeleteCompletedTrainingTitleOperation(
                completedTrainingTitleQueries = completedTrainingTitleQueries,
                completedTrainingTitleId = titleId,
            )
        }
    }

    fun updateTime(
        completedTrainingId: Long,
        startedAt: LocalDateTime,
        duration: Duration,
    ) = completedTrainingQueries
        .execute {
            it.updateTime(
                id = completedTrainingId,
                started_at = startedAt.toString(),
                duration = duration.toIsoString(),
            )
        }


    fun swapApproachOrdinals(
        approachFrom: Approach,
        approachTo: Approach,
        exerciseId: Long,
    ) = approachQueries
        .execute { approachQueries ->
            executeSwapApproachOrdinalsOperation(
                approachFrom = approachFrom,
                approachTo = approachTo,
                exerciseId = exerciseId,
                approachQueries = approachQueries,
            )
        }

    fun swapExerciseOrdinals(
        exerciseFrom: Exercise,
        exerciseTo: Exercise,
        trainingId: Long,
    ) = exerciseQueries
        .execute { exerciseQueries ->
            executeSwapExerciseOrdinalsOperation(
                exerciseFrom = exerciseFrom,
                exerciseTo = exerciseTo,
                trainingId = trainingId,
                exerciseQueries = exerciseQueries,
            )
        }
}
