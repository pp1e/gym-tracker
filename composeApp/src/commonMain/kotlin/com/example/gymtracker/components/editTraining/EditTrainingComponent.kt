package com.example.gymtracker.components.editTraining

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.gymtracker.database.databases.EditTrainingDatabase
import com.example.gymtracker.domain.CompletedTraining
import com.example.gymtracker.domain.TrainingProgramShort
import com.example.gymtracker.utils.asValue

class EditTrainingComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: EditTrainingDatabase,
    completedTrainingId: Long,
) : ComponentContext by componentContext {
    data class Model(
        val completedTraining: CompletedTraining?,
        val exerciseTemplateNames: List<String>,
        val exerciseName: String,
        val approachesCount: Int,
        val repetitionsCount: Int,
        val weight: Float,
    )

    private val store =
        instanceKeeper.getStore {
            EditTrainingStoreProvider(
                storeFactory = storeFactory,
                database = database,
                completedTrainingId = completedTrainingId,
            ).provide()
        }

    val model: Value<Model> = store.asValue(::stateToModel)

    fun requestExerciseDeleting(exerciseId: Long) {
        store.accept(
            EditTrainingStore.Intent.RequestExerciseDeleting(
                exerciseId = exerciseId,
            ),
        )
    }

    fun cancelExerciseDeleting(exerciseId: Long) {
        store.accept(
            EditTrainingStore.Intent.CancelExerciseDeleting(
                exerciseId = exerciseId,
            ),
        )
    }

    fun onApproachAdd(exerciseId: Long) {
        store.accept(EditTrainingStore.Intent.AddApproach(exerciseId))
    }

    fun requestApproachDeleting(approachId: Long) {
        store.accept(
            EditTrainingStore.Intent.RequestApproachDeleting(
                approachId = approachId,
            ),
        )
    }

    fun cancelApproachDeleting(approachId: Long) {
        store.accept(
            EditTrainingStore.Intent.CancelApproachDeleting(
                approachId = approachId,
            ),
        )
    }

    fun onAddExerciseClick() {
        store.accept(EditTrainingStore.Intent.AddExercise)
    }

    fun onExerciseNameChange(exerciseName: String) {
        store.accept(EditTrainingStore.Intent.ChangeExerciseName(exerciseName))
    }

    fun onApproachCountChange(approachesCount: Int) {
        store.accept(EditTrainingStore.Intent.ChangeApproachesCount(approachesCount))
    }

    fun onRepetitionsCountChange(repetitionsCount: Int) {
        store.accept(EditTrainingStore.Intent.ChangeRepetitionsCount(repetitionsCount))
    }

    fun onWeightChange(weight: Float) {
        store.accept(EditTrainingStore.Intent.ChangeWeight(weight))
    }

    fun onCompletedTrainingNameChange(name: String) {
        store.accept(EditTrainingStore.Intent.ChangeCompletedTrainingName(name))
    }

    fun onApproachWeightChange(
        approachId: Long,
        weight: Float,
    ) {
        store.accept(
            EditTrainingStore.Intent.ChangeApproachWeight(
                approachId = approachId,
                weight = weight,
            ),
        )
    }

    fun onApproachRepetitionsChange(
        approachId: Long,
        repetitions: Int,
    ) {
        store.accept(
            EditTrainingStore.Intent.ChangeApproachRepetitions(
                approachId = approachId,
                repetitions = repetitions,
            ),
        )
    }
}
