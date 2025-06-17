package com.example.gymtracker.database.databases

import app.cash.sqldelight.async.coroutines.awaitAsList
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.example.gymtracker.components.entities.ApproachInsert
import com.example.gymtracker.components.entities.ExerciseInsert
import com.example.gymtracker.components.entities.TrainingInsert
import com.example.gymtracker.database.operations.executeAddExerciseOperation
import com.example.gymtracker.database.operations.executeAddTrainingOperation
import com.example.gymtracker.database.operations.executeGetOrInsertCompletedTrainingTitleOperation
import com.example.gymtracker.database.operations.executeSwapApproachOrdinalsOperation
import com.example.gymtracker.database.operations.executeSwapExerciseOrdinalsOperation
import com.example.gymtracker.database.queryExecutors.executeGetCurrentTrainingQuery
import com.example.gymtracker.database.queryExecutors.executeGetExerciseTemplateListQuery
import com.example.gymtracker.database.queryExecutors.executeGetTrainingProgramListQuery
import com.example.gymtracker.database.queryExecutors.executeGetTrainingProgramQuery
import com.example.gymtracker.database.queryExecutors.executeGetTrainingScheduleQuery
import com.example.gymtracker.database.utils.awaitMaxId
import com.example.gymtracker.database.utils.execute
import com.example.gymtracker.database.utils.observe
import com.example.gymtracker.database.utils.zipAndExecute
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.domain.TrainingProgram
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.utils.currentDayOfWeek
import database.ApproachQueries
import database.CompletedTrainingQueries
import database.CompletedTrainingTitleQueries
import database.CurrentTrainingQueries
import database.ExerciseQueries
import database.ExerciseTemplateQueries
import database.TrainingProgramQueries
import database.TrainingQueries
import database.TrainingScheduleQueries
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toInstant

class CurrentTrainingDatabase(
    private val currentTrainingQueries: Single<CurrentTrainingQueries>,
    private val trainingProgramQueries: Single<TrainingProgramQueries>,
    private val trainingQueries: Single<TrainingQueries>,
    private val exerciseQueries: Single<ExerciseQueries>,
    private val approachQueries: Single<ApproachQueries>,
    private val exerciseTemplateQueries: Single<ExerciseTemplateQueries>,
    private val completedTrainingQueries: Single<CompletedTrainingQueries>,
    private val trainingScheduleQueries: Single<TrainingScheduleQueries>,
    private val completedTrainingTitleQueries: Single<CompletedTrainingTitleQueries>,
) {
    private suspend fun insertCurrentTrainingFromProgram(
        trainingProgram: TrainingProgram,
        currentTrainingQueries: CurrentTrainingQueries,
        approachQueries: ApproachQueries,
        exerciseQueries: ExerciseQueries,
        trainingQueries: TrainingQueries,
        coroutineScope: CoroutineScope,
    ) = currentTrainingQueries.insert(
        training_id =
            executeAddTrainingOperation(
                training =
                    TrainingInsert(
                        exercises =
                            trainingProgram.training.exercises.map { exercise ->
                                ExerciseInsert(
                                    ordinal = exercise.ordinal,
                                    exerciseTemplateId = exercise.template.id,
                                    approaches =
                                        exercise.approaches.map { approach ->
                                            ApproachInsert(
                                                ordinal = approach.ordinal,
                                                repetitions = approach.repetitions,
                                                weight = approach.weight,
                                            )
                                        },
                                )
                            },
                    ),
                approachQueries = approachQueries,
                exerciseQueries = exerciseQueries,
                trainingQueries = trainingQueries,
                coroutineScope = coroutineScope,
            ),
        name = trainingProgram.name,
    )

    private suspend fun deleteCurrentTrainingCascade(
        currentTrainingQueries: CurrentTrainingQueries,
        trainingQueries: TrainingQueries,
    ) {
        val trainingIds =
            currentTrainingQueries
                .getTrainingIds()
                .awaitAsList()
        currentTrainingQueries.clean()
        trainingQueries.delete(trainingIds)
    }

    fun observeCurrentTraining() =
        currentTrainingQueries
            .map(CurrentTrainingQueries::get)
            .observe(::executeGetCurrentTrainingQuery)

    fun observeTrainingProgramList() =
        trainingProgramQueries
            .map(TrainingProgramQueries::getList)
            .observe(::executeGetTrainingProgramListQuery)

    fun observeExerciseTemplateList() =
        exerciseTemplateQueries
            .map(ExerciseTemplateQueries::getList)
            .observe(::executeGetExerciseTemplateListQuery)

    fun startTraining() =
        zipAndExecute(
            trainingScheduleQueries,
            currentTrainingQueries,
            trainingQueries,
            exerciseQueries,
            approachQueries,
        ) { trainingScheduleQueries, currentTrainingQueries, trainingQueries, exerciseQueries, approachQueries ->
            deleteCurrentTrainingCascade(
                currentTrainingQueries = currentTrainingQueries,
                trainingQueries = trainingQueries,
            )

            val trainingProgram =
                executeGetTrainingScheduleQuery(
                    getTrainingScheduleQuery =
                        trainingScheduleQueries.get(
                            dayOfWeek = currentDayOfWeek().isoDayNumber.toLong(),
                        ),
                )
            if (trainingProgram != null) {
                insertCurrentTrainingFromProgram(
                    trainingProgram = trainingProgram,
                    currentTrainingQueries = currentTrainingQueries,
                    trainingQueries = trainingQueries,
                    approachQueries = approachQueries,
                    exerciseQueries = exerciseQueries,
                    coroutineScope = this,
                )
            } else {
                val trainingId =
                    trainingQueries
                        .maxId()
                        .awaitMaxId { it.MAX }
                trainingQueries.insert(
                    id = trainingId,
                )
                currentTrainingQueries.insertDefaults(
                    training_id = trainingId,
                    default_name = I18nManager.strings.unnamedTraining,
                )
            }
        }

    fun setTrainingByProgram(trainingProgramId: Long) =
        zipAndExecute(
            trainingProgramQueries,
            currentTrainingQueries,
            trainingQueries,
            exerciseQueries,
            approachQueries,
        ) { trainingProgramQueries, currentTrainingQueries, trainingQueries, exerciseQueries, approachQueries ->
            executeGetTrainingProgramQuery(
                getTrainingProgramQuery = trainingProgramQueries.get(trainingProgramId),
            )?.let { trainingProgram ->
                deleteCurrentTrainingCascade(
                    currentTrainingQueries = currentTrainingQueries,
                    trainingQueries = trainingQueries,
                )

                insertCurrentTrainingFromProgram(
                    trainingProgram = trainingProgram,
                    currentTrainingQueries = currentTrainingQueries,
                    trainingQueries = trainingQueries,
                    approachQueries = approachQueries,
                    exerciseQueries = exerciseQueries,
                    coroutineScope = this,
                )
            }
        }

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

    fun updateCurrentTrainingName(name: String) =
        currentTrainingQueries
            .execute {
                it.updateName(name)
            }

    fun updateStartedAt(startedAt: LocalDateTime) =
        currentTrainingQueries
            .execute {
                it.updateStartedAt(startedAt.toString())
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

    fun moveTrainingToHistory(
        name: String,
        trainingId: Long,
        startedAt: LocalDateTime,
    ) = zipAndExecute(
        completedTrainingQueries,
        currentTrainingQueries,
        completedTrainingTitleQueries,
    ) { completedTrainingQueries, currentTrainingQueries, completedTrainingTitleQueries ->
        val titleId =
            executeGetOrInsertCompletedTrainingTitleOperation(
                trainingName = name,
                completedTrainingTitleQueries = completedTrainingTitleQueries,
            )

        completedTrainingQueries
            .insert(
                title_id = titleId,
                training_id = trainingId,
                started_at = startedAt.toString(),
                duration =
                    Clock.System.now()
                        .minus(
                            startedAt.toInstant(
                                TimeZone.currentSystemDefault(),
                            ),
                        )
                        .toIsoString(),
            )
        currentTrainingQueries
            .clean()
    }

    fun deleteTraining() =
        zipAndExecute(
            currentTrainingQueries,
            trainingQueries,
        ) { currentTrainingQueries, trainingQueries ->
            deleteCurrentTrainingCascade(
                currentTrainingQueries = currentTrainingQueries,
                trainingQueries = trainingQueries,
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
