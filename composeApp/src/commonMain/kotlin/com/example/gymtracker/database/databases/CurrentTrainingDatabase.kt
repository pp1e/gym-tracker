package com.example.gymtracker.database.databases

import app.cash.sqldelight.async.coroutines.awaitAsList
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.zip
import com.example.gymtracker.components.entities.ApproachInsert
import com.example.gymtracker.components.entities.ExerciseInsert
import com.example.gymtracker.components.entities.TrainingInsert
import com.example.gymtracker.database.QueryTuple5
import com.example.gymtracker.database.awaitMaxId
import com.example.gymtracker.database.queryExecutors.executeGetCurrentTrainingQuery
import com.example.gymtracker.database.execute
import com.example.gymtracker.database.observe
import com.example.gymtracker.database.operations.executeAddExerciseOperation
import com.example.gymtracker.database.operations.executeAddTrainingOperation
import com.example.gymtracker.database.queryExecutors.executeGetExerciseTemplateListQuery
import com.example.gymtracker.database.queryExecutors.executeGetTrainingProgramListQuery
import com.example.gymtracker.database.queryExecutors.executeGetTrainingProgramQuery
import database.ApproachQueries
import database.CurrentTrainingQueries
import database.ExerciseQueries
import database.ExerciseTemplateQueries
import database.CompleteTrainingQueries
import database.TrainingProgramQueries
import database.TrainingQueries
import kotlinx.datetime.Clock
import kotlinx.datetime.isoDayNumber

class CurrentTrainingDatabase(
    private val currentTrainingQueries: Single<CurrentTrainingQueries>,
    private val trainingProgramQueries: Single<TrainingProgramQueries>,
    private val trainingQueries: Single<TrainingQueries>,
    private val exerciseQueries: Single<ExerciseQueries>,
    private val approachQueries: Single<ApproachQueries>,
    private val exerciseTemplateQueries: Single<ExerciseTemplateQueries>,
    private val completeTrainingQueries: Single<CompleteTrainingQueries>,
) {
    private suspend fun deleteCurrentTrainingCascade(
        currentTrainingQueries: CurrentTrainingQueries,
        trainingQueries: TrainingQueries,
    ) {
        val trainingIds = currentTrainingQueries
                    .getTrainingIds()
                    .awaitAsList()
        currentTrainingQueries.clean()
        trainingQueries.delete(trainingIds)
    }

    fun observeCurrentTraining() =
        currentTrainingQueries
            .map(CurrentTrainingQueries::get)
            .observe(::executeGetCurrentTrainingQuery)

    fun observeTrainingProgramList() = trainingProgramQueries
        .map(TrainingProgramQueries::getList)
        .observe(::executeGetTrainingProgramListQuery)

    fun observeExerciseTemplateList() = exerciseTemplateQueries
        .map(ExerciseTemplateQueries::getList)
        .observe(::executeGetExerciseTemplateListQuery)

    fun createAndSetEmptyTraining() = zip(
        currentTrainingQueries,
        trainingQueries,
    ) { currentTrainingQueries, trainingQueries ->
        Pair(currentTrainingQueries, trainingQueries)
    }
        .execute { (currentTrainingQueries, trainingQueries) ->
            deleteCurrentTrainingCascade(
                currentTrainingQueries = currentTrainingQueries,
                trainingQueries = trainingQueries,
            )

            val trainingId = trainingQueries
                .maxId()
                .awaitMaxId { it.MAX }
            trainingQueries.insert(
                id = trainingId
            )
            currentTrainingQueries.insertDefaults(
                training_id = trainingId,
            )
        }

    fun setTrainingByProgram(
        trainingProgramId: Long,
    ) = zip(
        trainingProgramQueries,
        currentTrainingQueries,
        trainingQueries,
        exerciseQueries,
        approachQueries,
    ) { trainingProgramQueries, currentTrainingQueries, trainingQueries, exerciseQueries, approachQueries ->
        QueryTuple5(trainingProgramQueries, currentTrainingQueries, trainingQueries, exerciseQueries, approachQueries)
    }
        .execute { (trainingProgramQueries, currentTrainingQueries, trainingQueries, exerciseQueries, approachQueries) ->
            executeGetTrainingProgramQuery(
                getTrainingProgramQuery = trainingProgramQueries.get(trainingProgramId),
            )?.let { trainingProgram ->
                deleteCurrentTrainingCascade(
                    currentTrainingQueries = currentTrainingQueries,
                    trainingQueries = trainingQueries,
                )

                currentTrainingQueries.insert(
                    training_id = executeAddTrainingOperation(
                        training = TrainingInsert(
                            exercises = trainingProgram.training.exercises.map { exercise ->
                                ExerciseInsert(
                                    ordinal = exercise.ordinal,
                                    exerciseTemplateId = exercise.template.id,
                                    approaches = exercise.approaches.map { approach ->
                                        ApproachInsert(
                                            ordinal = approach.ordinal,
                                            repetitions = approach.repetitions,
                                            weight = approach.weight,
                                        )
                                    }
                                )
                            }
                        ),
                        approachQueries = approachQueries,
                        exerciseQueries = exerciseQueries,
                        trainingQueries = trainingQueries,
                        coroutineScope = this,
                    ),
                    name = trainingProgram.name,
                )
            }
        }

    fun addExercise(
        trainingId: Long,
        exerciseTemplate: NewOrExistingExerciseTemplate,
        approachesCount: Int,
        repetitionsCount: Int,
        weight: Float?,
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

    fun addApproach(exerciseId: Long) = approachQueries
        .execute {
            it.insertSingle(
                exercise_id = exerciseId,
                weight = null,
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
        weight: Float?,
    ) = approachQueries
        .execute {
            it.updateWeight(
                id = approachId,
                weight = weight?.toDouble(),
            )
        }

    fun updateCurrentTrainingName(
        name: String,
    ) = currentTrainingQueries
        .execute {
            it.updateName(name)
        }

    fun deleteApproach(
        approachId: Long,
    ) = approachQueries
        .execute {
            it.delete(approachId)
        }

    fun deleteExercise(
        exerciseId: Long,
    ) = exerciseQueries
        .execute {
            it.delete(exerciseId)
        }

    fun moveTrainingToHistory(
        name: String,
        trainingId: Long
    ) = zip(
        completeTrainingQueries,
        currentTrainingQueries,
    ) { completeTrainingQueries, currentTrainingQueries ->
        Pair(completeTrainingQueries, currentTrainingQueries)
    }.execute { (completeTrainingQueries, currentTrainingQueries) ->
        completeTrainingQueries
            .insert(
                name = name,
                training_id = trainingId,
            )
        currentTrainingQueries
            .clean()
    }
}
