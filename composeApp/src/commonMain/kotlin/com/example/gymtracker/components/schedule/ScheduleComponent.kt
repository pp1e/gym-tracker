package com.example.gymtracker.components.schedule

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.gymtracker.database.databases.ScheduleDatabase
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.domain.TrainingProgram
import com.example.gymtracker.domain.TrainingProgramShort
import com.example.gymtracker.utils.asValue

class ScheduleComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: ScheduleDatabase,
) : ComponentContext by componentContext {
    data class Model(
        val trainingProgram: TrainingProgram?,
        val trainingProgramsShort: List<TrainingProgramShort>,
        val exerciseTemplateNames: List<String>,
        val exerciseName: String,
        val approachesCount: Int,
        val repetitionsCount: Int,
        val weight: Float,
    )

    private val store =
        instanceKeeper.getStore {
            ScheduleStoreProvider(
                storeFactory = storeFactory,
                database = database,
            ).provide()
        }

    val model: Value<Model> = store.asValue(::stateToModel)

    fun createNewTrainingProgram() {
        store.accept(ScheduleStore.Intent.CreateNewTrainingProgram)
    }

    fun changeTrainingProgram(trainingProgramId: Long) {
        store.accept(ScheduleStore.Intent.ChangeTrainingProgram(trainingProgramId))
    }

    fun requestExerciseDeleting(exerciseId: Long) {
        store.accept(
            ScheduleStore.Intent.RequestExerciseDeleting(
                exerciseId = exerciseId,
            ),
        )
    }

    fun cancelExerciseDeleting(exerciseId: Long) {
        store.accept(
            ScheduleStore.Intent.CancelExerciseDeleting(
                exerciseId = exerciseId,
            ),
        )
    }

    fun onApproachAdd(exerciseId: Long) {
        store.accept(ScheduleStore.Intent.AddApproach(exerciseId))
    }

    fun requestApproachDeleting(approachId: Long) {
        store.accept(
            ScheduleStore.Intent.RequestApproachDeleting(
                approachId = approachId,
            ),
        )
    }

    fun cancelApproachDeleting(approachId: Long) {
        store.accept(
            ScheduleStore.Intent.CancelApproachDeleting(
                approachId = approachId,
            ),
        )
    }

    fun onAddExerciseClick() {
        store.accept(ScheduleStore.Intent.AddExercise)
    }

    fun onExerciseNameChange(exerciseName: String) {
        store.accept(ScheduleStore.Intent.ChangeExerciseName(exerciseName))
    }

    fun onApproachCountChange(approachesCount: Int) {
        store.accept(ScheduleStore.Intent.ChangeApproachesCount(approachesCount))
    }

    fun onRepetitionsCountChange(repetitionsCount: Int) {
        store.accept(ScheduleStore.Intent.ChangeRepetitionsCount(repetitionsCount))
    }

    fun onWeightChange(weight: Float) {
        store.accept(ScheduleStore.Intent.ChangeWeight(weight))
    }

    fun onTrainingProgramNameChange(name: String) {
        store.accept(ScheduleStore.Intent.ChangeTrainingProgramName(name))
    }

    fun onApproachWeightChange(
        approachId: Long,
        weight: Float,
    ) {
        store.accept(
            ScheduleStore.Intent.ChangeApproachWeight(
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
            ScheduleStore.Intent.ChangeApproachRepetitions(
                approachId = approachId,
                repetitions = repetitions,
            ),
        )
    }

    fun onApproachesSwap(
        approachFrom: Approach,
        approachTo: Approach,
        exerciseId: Long,
    ) {
        store.accept(
            ScheduleStore.Intent.SwapApproachOrdinals(
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
            ScheduleStore.Intent.SwapExerciseOrdinals(
                exerciseFrom = exerciseFrom,
                exerciseTo = exerciseTo,
            ),
        )
    }
}
