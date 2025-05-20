package com.example.gymtracker.components.currentTraining

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import com.example.gymtracker.database.databases.CurrentTrainingDatabase
import com.example.gymtracker.domain.CurrentTraining
import com.example.gymtracker.domain.TrainingProgramShort
import com.example.gymtracker.utils.asValue
import kotlinx.datetime.LocalDateTime

class CurrentTrainingComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: CurrentTrainingDatabase,
    private val output: Consumer<Output>,
) : ComponentContext by componentContext {
    data class Model(
        val currentTraining: CurrentTraining?,
        val trainingProgramsShort: List<TrainingProgramShort>,
        val exerciseTemplateNames: List<String>,
        val exerciseName: String,
        val approachesCount: Int,
        val repetitionsCount: Int,
        val weight: Float,
        val isTrainingIrrelevant: Boolean,
    )

    sealed class Output {
        data object HistoryTransit : Output()
    }

    private val store =
        instanceKeeper.getStore {
            CurrentTrainingStoreProvider(
                storeFactory = storeFactory,
                database = database,
            ).provide()
        }

    val model: Value<Model> = store.asValue(::stateToModel)

    fun onStartTrainingClick() {
        store.accept(CurrentTrainingStore.Intent.StartTraining)
    }

    fun changeTrainingProgram(trainingProgramId: Long) {
        store.accept(CurrentTrainingStore.Intent.ChangeTrainingProgram(trainingProgramId))
    }

    fun requestExerciseDeleting(exerciseId: Long) {
        store.accept(
            CurrentTrainingStore.Intent.RequestExerciseDeleting(
                exerciseId = exerciseId,
            ),
        )
    }

    fun cancelExerciseDeleting(exerciseId: Long) {
        store.accept(
            CurrentTrainingStore.Intent.CancelExerciseDeleting(
                exerciseId = exerciseId,
            ),
        )
    }

    fun onApproachAdd(exerciseId: Long) {
        store.accept(CurrentTrainingStore.Intent.AddApproach(exerciseId))
    }

    fun requestApproachDeleting(approachId: Long) {
        store.accept(
            CurrentTrainingStore.Intent.RequestApproachDeleting(
                approachId = approachId,
            ),
        )
    }

    fun cancelApproachDeleting(approachId: Long) {
        store.accept(
            CurrentTrainingStore.Intent.CancelApproachDeleting(
                approachId = approachId,
            ),
        )
    }

    fun onAddExerciseClick() {
        store.accept(CurrentTrainingStore.Intent.AddExercise)
    }

    fun onExerciseNameChange(exerciseName: String) {
        store.accept(CurrentTrainingStore.Intent.ChangeExerciseName(exerciseName))
    }

    fun onApproachCountChange(approachesCount: Int) {
        store.accept(CurrentTrainingStore.Intent.ChangeApproachesCount(approachesCount))
    }

    fun onRepetitionsCountChange(repetitionsCount: Int) {
        store.accept(CurrentTrainingStore.Intent.ChangeRepetitionsCount(repetitionsCount))
    }

    fun onWeightChange(weight: Float) {
        store.accept(CurrentTrainingStore.Intent.ChangeWeight(weight))
    }

    fun onCurrentTrainingNameChange(name: String) {
        store.accept(CurrentTrainingStore.Intent.ChangeCurrentTrainingName(name))
    }

    fun onApproachWeightChange(
        approachId: Long,
        weight: Float,
    ) {
        store.accept(
            CurrentTrainingStore.Intent.ChangeApproachWeight(
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
            CurrentTrainingStore.Intent.ChangeApproachRepetitions(
                approachId = approachId,
                repetitions = repetitions,
            ),
        )
    }

    fun onCompleteTrainingClick() {
        store.accept(CurrentTrainingStore.Intent.SaveTrainingToHistory)
        output(Output.HistoryTransit)
    }

    fun onDeleteTrainingClick() {
        store.accept(CurrentTrainingStore.Intent.DeleteTraining)
    }

    fun onStartedAtUpdate(startedAt: LocalDateTime) {
        store.accept(
            CurrentTrainingStore.Intent.UpdateStartedAt(startedAt)
        )
    }
}
