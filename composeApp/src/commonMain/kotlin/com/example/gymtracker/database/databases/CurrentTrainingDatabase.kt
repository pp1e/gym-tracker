package com.example.gymtracker.database.databases

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.example.gymtracker.database.queryExecutors.executeGetCurrentTrainingQuery
import com.example.gymtracker.database.execute
import com.example.gymtracker.database.observe
import database.ApproachQueries
import database.CurrentTrainingQueries
import database.ExerciseQueries
import database.TrainingQueries

class CurrentTrainingDatabase(
    private val currentTrainingQueries: Single<CurrentTrainingQueries>,
    private val trainingQueries: Single<TrainingQueries>,
    private val exerciseQueries: Single<ExerciseQueries>,
    private val approachQueries: Single<ApproachQueries>,
) {
    fun observeTraining() =
        currentTrainingQueries
            .map(CurrentTrainingQueries::get)
            .observe(::executeGetCurrentTrainingQuery)

//    fun setTraining(
//        training: TrainingInsert,
//    ) =
//        zip(
//            currentTrainingQueries,
//            trainingQueries,
//            exerciseQueries,
//            approachQueries,
//        ) { currentTrainingQueries, trainingQueries, exerciseQueries, approachQueries ->
//            QueryTuple4(currentTrainingQueries, trainingQueries, exerciseQueries, approachQueries)
//        }
//            .execute { (currentTrainingQueries, trainingQueries, exerciseQueries, approachQueries) ->
//                val trainingIds = currentTrainingQueries
//                    .getTrainingIds()
//                    .awaitAsList()
//                currentTrainingQueries.clean()
//                trainingQueries.delete(trainingIds)
//
//                currentTrainingQueries.insert(
//                    started_at = Clock.System.now().toString(),
//                    program_name = "",
//                    training_id = executeAddTrainingOperation(
//                        training = training,
//                        trainingQueries = trainingQueries,
//                        exerciseQueries = exerciseQueries,
//                        approachQueries = approachQueries,
//                    ),
//                )
//            }

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
}
