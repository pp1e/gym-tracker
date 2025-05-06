package com.example.gymtracker.components.editTraining

import com.arkivanov.mvikotlin.core.store.Store
import com.example.gymtracker.domain.CompletedTraining
import com.example.gymtracker.domain.ExerciseTemplate

internal interface EditTrainingStore : Store<EditTrainingStore.Intent, EditTrainingStore.State, Nothing> {
    sealed class Intent {
        data object AddExercise : Intent()

        data class AddApproach(val exerciseId: Long) : Intent()

        data class ChangeApproachRepetitions(
            val approachId: Long,
            val repetitions: Int,
        ) : Intent()

        data class ChangeApproachWeight(
            val approachId: Long,
            val weight: Float,
        ) : Intent()

        data class ChangeCompletedTrainingName(
            val name: String,
        ) : Intent()

        data class RequestApproachDeleting(
            val approachId: Long,
        ) : Intent()

        data class CancelApproachDeleting(
            val approachId: Long,
        ) : Intent()

        data class RequestExerciseDeleting(
            val exerciseId: Long,
        ) : Intent()

        data class CancelExerciseDeleting(
            val exerciseId: Long,
        ) : Intent()

        data class ChangeExerciseName(
            val name: String,
        ) : Intent()

        data class ChangeApproachesCount(
            val approachesCount: Int,
        ) : Intent()

        data class ChangeRepetitionsCount(
            val repetitionsCount: Int,
        ) : Intent()

        data class ChangeWeight(
            val weight: Float,
        ) : Intent()
    }

    data class State(
        val completedTraining: CompletedTraining? = null,
        val exerciseTemplates: List<ExerciseTemplate> = emptyList(),
        val exerciseName: String = "Новое упражнение",
        val approachesCount: Int = 4,
        val repetitionsCount: Int = 10,
        val weight: Float = 0f,
        val deleteApproachRequests: List<Long> = emptyList(),
        val deleteExerciseRequests: List<Long> = emptyList(),
    )
}
