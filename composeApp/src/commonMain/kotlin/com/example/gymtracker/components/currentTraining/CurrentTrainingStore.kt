package com.example.gymtracker.components.currentTraining

import com.arkivanov.mvikotlin.core.store.Store
import com.example.gymtracker.domain.CurrentTraining
import com.example.gymtracker.domain.ExerciseTemplate
import com.example.gymtracker.domain.TrainingProgramShort

internal interface CurrentTrainingStore : Store<CurrentTrainingStore.Intent, CurrentTrainingStore.State, Nothing> {
    sealed class Intent {
        data object AddExercise: Intent()

        data class AddApproach(val exerciseId: Long) : Intent()

        data class ChangeApproachRepetitions(
            val approachId: Long,
            val repetitions: Int,
        ) : Intent()

        data class ChangeApproachWeight(
            val approachId: Long,
            val weight: Float,
        ) : Intent()

        data class ChangeCurrentTrainingName(
            val name: String,
        ) : Intent()

        data class ChangeTrainingProgram(
            val trainingProgramId: Long,
        ): Intent()

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

        data object CreateNewTraining: Intent()

        data object SaveTrainingToHistory: Intent()
    }

    data class State(
        val currentTraining: CurrentTraining? = null,
        val trainingProgramsShort: List<TrainingProgramShort> = emptyList(),

        val exerciseTemplates: List<ExerciseTemplate> = emptyList(),
        val exerciseName: String = "Новое упражнение",
        val approachesCount: Int = 4,
        val repetitionsCount: Int = 10,
        val weight: Float = 0f,

        val deleteApproachRequests: List<Long> = emptyList(),
        val deleteExerciseRequests: List<Long> = emptyList(),
    )
}
