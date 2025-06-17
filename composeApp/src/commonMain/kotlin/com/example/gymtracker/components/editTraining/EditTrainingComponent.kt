package com.example.gymtracker.components.editTraining

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import com.example.gymtracker.database.databases.EditTrainingDatabase
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.CompletedTraining
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.utils.asValue
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

class EditTrainingComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: EditTrainingDatabase,
    completedTrainingId: Long,
    private val output: Consumer<Output>,
) : ComponentContext by componentContext {
    data class Model(
        val completedTraining: CompletedTraining?,
        val exerciseTemplateNames: List<String>,
        val exerciseName: String,
        val approachesCount: Int,
        val repetitionsCount: Int,
        val weight: Float,
    )

    sealed class Output {
        data object HistoryTransit : Output()
    }

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

    fun onDeleteTrainingClick() {
        store.accept(EditTrainingStore.Intent.DeleteTraining)
        output(Output.HistoryTransit)
    }

    fun onTimeUpdate(
        startedAt: LocalDateTime,
        duration: Duration,
    ) {
        store.accept(
            EditTrainingStore.Intent.UpdateTime(
                startedAt = startedAt,
                duration = duration,
            ),
        )
    }

    fun onApproachesSwap(
        approachFrom: Approach,
        approachTo: Approach,
        exerciseId: Long,
    ) {
        store.accept(
            EditTrainingStore.Intent.SwapApproachOrdinals(
                approachFrom = approachFrom,
                approachTo = approachTo,
                exerciseId = exerciseId,
            ),
        )
    }

    fun onExercisesSwap(
        exerciseFrom: Exercise,
        exerciseTo: Exercise,
    ) {
        store.accept(
            EditTrainingStore.Intent.SwapExerciseOrdinals(
                exerciseFrom = exerciseFrom,
                exerciseTo = exerciseTo,
            ),
        )
    }
}
