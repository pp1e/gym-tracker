package com.example.gymtracker.database.databases

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOne
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.zip
import com.example.gymtracker.database.awaitMaxId
import com.example.gymtracker.database.execute
import com.example.gymtracker.database.observe
import com.example.gymtracker.database.queryExecutors.executeGetExerciseTemplateListQuery
import com.example.gymtracker.database.queryExecutors.executeGetTrainingProgramListQuery
import com.example.gymtracker.database.queryExecutors.executeGetTrainingScheduleQuery
import database.ApproachQueries
import database.ExerciseQueries
import database.ExerciseTemplateQueries
import database.TrainingProgramQueries
import database.TrainingQueries
import database.TrainingScheduleQueries
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

sealed class NewOrExistingExerciseTemplate{
    data class NewExerciseTemplate(
        val name: String
    ) : NewOrExistingExerciseTemplate()

    data class ExistingExerciseTemplate(
        val id: Long
    ) : NewOrExistingExerciseTemplate()
}

class ScheduleDatabase(
    private val trainingScheduleQueries: Single<TrainingScheduleQueries>,
    private val trainingProgramQueries: Single<TrainingProgramQueries>,
    private val trainingQueries: Single<TrainingQueries>,
    private val exerciseQueries: Single<ExerciseQueries>,
    private val approachQueries: Single<ApproachQueries>,
    private val exerciseTemplateQueries: Single<ExerciseTemplateQueries>,
    private val dayOfWeek: DayOfWeek,
) {
    fun observeTrainingSchedule() = trainingScheduleQueries
        .map { it.get(dayOfWeek.isoDayNumber.toLong()) }
        .observe(::executeGetTrainingScheduleQuery)

    fun observeTrainingProgramList() = trainingProgramQueries
        .map(TrainingProgramQueries::getList)
        .observe(::executeGetTrainingProgramListQuery)

    fun observeExerciseTemplateList() = exerciseTemplateQueries
        .map(ExerciseTemplateQueries::getList)
        .observe(::executeGetExerciseTemplateListQuery)

    fun createAndSetEmptyProgram() = zip(
        trainingScheduleQueries,
        trainingProgramQueries,
        trainingQueries,
    ) { trainingScheduleQueries, trainingProgramQueries, trainingQueries ->
        Triple(trainingScheduleQueries, trainingProgramQueries, trainingQueries)
    }
        .execute { (trainingScheduleQueries, trainingProgramQueries, trainingQueries) ->
            val trainingId = trainingQueries
                .maxId()
                .awaitMaxId { it.MAX }
            trainingQueries.insert(
                id = trainingId
            )
            val trainingProgramId = trainingProgramQueries
                .maxId()
                .awaitMaxId { it.MAX }
            trainingProgramQueries.insert(
                id = trainingProgramId,
                training_id = trainingId,
            )
            trainingScheduleQueries
                .insert(
                    dayOfWeek = dayOfWeek.isoDayNumber.toLong(),
                    training_program_id = trainingProgramId,
                )
        }

    fun setProgram(
        trainingProgramId: Long,
    ) = trainingScheduleQueries
        .execute {
            it
                .insert(
                    dayOfWeek = dayOfWeek.isoDayNumber.toLong(),
                    training_program_id = trainingProgramId,
                )
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
        val exerciseId = exerciseQueries
            .maxId()
            .awaitMaxId { it.MAX }
        exerciseQueries.insertSingle(
            id = exerciseId,
            training_id = trainingId,
            exercise_template_id = when (exerciseTemplate) {
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
            launch {
                approachQueries
                    .insert(
                        ordinal = ordinal++,
                        exercise_id = exerciseId,
                        repetitions = repetitionsCount.toLong(),
                        weight = weight?.toDouble(),
                    )
            }
        }.joinAll()
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

    fun updateTrainingProgramName(
        id: Long,
        name: String,
    ) = trainingProgramQueries
        .execute {
            it.updateName(
                id = id,
                name = name,
            )
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
}
