package com.example.gymtracker.database.databases

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.example.gymtracker.database.operations.executeAddExerciseOperation
import com.example.gymtracker.database.operations.executeSwapApproachOrdinalsOperation
import com.example.gymtracker.database.operations.executeSwapExerciseOrdinalsOperation
import com.example.gymtracker.database.queryExecutors.executeGetExerciseTemplateListQuery
import com.example.gymtracker.database.queryExecutors.executeGetTrainingProgramListQuery
import com.example.gymtracker.database.queryExecutors.executeGetTrainingScheduleQuery
import com.example.gymtracker.database.utils.awaitMaxId
import com.example.gymtracker.database.utils.execute
import com.example.gymtracker.database.utils.observe
import com.example.gymtracker.database.utils.zipAndExecute
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.i18n.I18nManager
import database.ApproachQueries
import database.ExerciseQueries
import database.ExerciseTemplateQueries
import database.TrainingProgramQueries
import database.TrainingQueries
import database.TrainingScheduleQueries
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.isoDayNumber

sealed class NewOrExistingExerciseTemplate {
    data class NewExerciseTemplate(
        val name: String,
    ) : NewOrExistingExerciseTemplate()

    data class ExistingExerciseTemplate(
        val id: Long,
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
    fun observeTrainingSchedule() =
        trainingScheduleQueries
            .map { it.get(dayOfWeek.isoDayNumber.toLong()) }
            .observe(::executeGetTrainingScheduleQuery)

    fun observeTrainingProgramList() =
        trainingProgramQueries
            .map(TrainingProgramQueries::getList)
            .observe(::executeGetTrainingProgramListQuery)

    fun observeExerciseTemplateList() =
        exerciseTemplateQueries
            .map(ExerciseTemplateQueries::getList)
            .observe(::executeGetExerciseTemplateListQuery)

    fun createAndSetEmptyProgram() =
        zipAndExecute(
            trainingScheduleQueries,
            trainingProgramQueries,
            trainingQueries,
        ) { trainingScheduleQueries, trainingProgramQueries, trainingQueries ->
            val trainingId =
                trainingQueries
                    .maxId()
                    .awaitMaxId { it.MAX }
            trainingQueries.insert(
                id = trainingId,
            )
            val trainingProgramId =
                trainingProgramQueries
                    .maxId()
                    .awaitMaxId { it.MAX }
            trainingProgramQueries.insert(
                id = trainingProgramId,
                training_id = trainingId,
                default_name = I18nManager.strings.unnamedProgram,
            )
            trainingScheduleQueries
                .insert(
                    dayOfWeek = dayOfWeek.isoDayNumber.toLong(),
                    training_program_id = trainingProgramId,
                )
        }

    fun setProgram(trainingProgramId: Long) =
        trainingScheduleQueries
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
