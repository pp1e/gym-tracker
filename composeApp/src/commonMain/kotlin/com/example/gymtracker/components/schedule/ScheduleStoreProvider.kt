package com.example.gymtracker.components.schedule

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.andThen
import com.badoo.reaktive.completable.completableDefer
import com.badoo.reaktive.completable.completableFromFunction
import com.badoo.reaktive.completable.completableOfEmpty
import com.badoo.reaktive.completable.completableTimer
import com.badoo.reaktive.completable.delay
import com.badoo.reaktive.completable.doOnAfterComplete
import com.badoo.reaktive.completable.doOnAfterFinally
import com.badoo.reaktive.completable.observeOn
import com.badoo.reaktive.completable.subscribeOn
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.example.gymtracker.database.databases.NewOrExistingExerciseTemplate
import com.example.gymtracker.database.databases.ScheduleDatabase
import com.example.gymtracker.domain.ExerciseTemplate
import com.example.gymtracker.domain.TrainingProgram
import com.example.gymtracker.domain.TrainingProgramShort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class ScheduleStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: ScheduleDatabase,
) {
    fun provide(): ScheduleStore =
        object :
            ScheduleStore,
            Store<ScheduleStore.Intent, ScheduleStore.State, Nothing> by storeFactory.create(
                name = "ScheduleStore",
                initialState = ScheduleStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class TrainingProgramLoaded(
            val trainingProgram: TrainingProgram?
        ) : Msg()

        data class TrainingProgramsShortLoaded(
            val trainingProgramsShort: List<TrainingProgramShort>
        ) : Msg()

        data class ExerciseTemplatesLoaded(
            val exerciseTemplates: List<ExerciseTemplate>,
        ) : Msg()

        data class ApproachesCountChanged(
            val approachesCount: Int,
        ) : Msg()

        data class RepetitionsCountChanged(
            val repetitionsCount: Int,
        ) : Msg()

        data class WeightChanged(
            val weight: Float,
        ) : Msg()

        data class ExerciseNameChanged(
            val exerciseName: String,
        ) : Msg()

        data class TrainingProgramNameChanged(
            val name: String,
        ) : Msg()

        data class ApproachDeletingFinished(
            val approachId: Long,
        ) : Msg()

        data class ExerciseDeletingFinished(
            val exerciseId: Long,
        ) : Msg()

        data class ApproachDeletingRequested(
            val approachId: Long,
        ) : Msg()

        data class ExerciseDeletingRequested(
            val exerciseId: Long,
        ) : Msg()

//        data object ExerciseAdded : Msg()
//
//        data class ApproachAdded(val exerciseId: Long): Msg()
//
//        data class RepetitionsChanged(
//            val exerciseId: Long,
//            val repetitions: Int,
//        ): Msg()
//
//        data class WeightChanged(
//            val exerciseId: Long,
//            val weight: Float,
//        ): Msg()
//
//        data class ApproachDeleted(
//            val exerciseId: Long,
//            val approachId: Long,
//        ): Msg()

//        data class ExerciseDeleted(val exerciseId: Long): Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<ScheduleStore.Intent, Unit, ScheduleStore.State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> ScheduleStore.State) {
            database
                .observeTrainingSchedule()
                .observeOn(mainScheduler)
                .map(Msg::TrainingProgramLoaded)
                .subscribeScoped(
                    onNext = { trainingProgramLoadedMessage ->
                        dispatch(trainingProgramLoadedMessage)
                    }
                )

            database
                .observeTrainingProgramList()
                .observeOn(mainScheduler)
                .map(Msg::TrainingProgramsShortLoaded)
                .subscribeScoped(
                    onNext = { trainingProgramsShortLoadedMessage ->
                        dispatch(trainingProgramsShortLoadedMessage)
                    }
                )

            database
                .observeExerciseTemplateList()
                .observeOn(mainScheduler)
                .map(Msg::ExerciseTemplatesLoaded)
                .subscribeScoped(
                    onNext = { exerciseTemplatesLoadedMessage ->
                        dispatch(exerciseTemplatesLoadedMessage)
                    }
                )
        }

        override fun executeIntent(intent: ScheduleStore.Intent, getState: () -> ScheduleStore.State) =
            when (intent) {
                is ScheduleStore.Intent.CreateNewTrainingProgram -> createNewTrainingProgram()
                is ScheduleStore.Intent.ChangeTrainingProgram -> changeTrainingProgram(
                    trainingProgramId = intent.trainingProgramId,
                )
                is ScheduleStore.Intent.AddExercise -> addExercise(
                    getState = getState,
                )
                is ScheduleStore.Intent.AddApproach -> addApproach(
                    exerciseId = intent.exerciseId,
                )
                is ScheduleStore.Intent.ChangeApproachWeight -> changeWeight(
                    approachId = intent.approachId,
                    weight = intent.weight,
                )
                is ScheduleStore.Intent.ChangeApproachRepetitions -> changeRepetitions(
                    approachId = intent.approachId,
                    repetitions = intent.repetitions,
                )
                is ScheduleStore.Intent.ChangeTrainingProgramName -> changeTrainingProgramName(
                    name = intent.name,
                    getState = getState,
                )
                is ScheduleStore.Intent.RequestApproachDeleting -> requestApproachDeleting(
                    approachId = intent.approachId,
                    getState = getState,
                )
                is ScheduleStore.Intent.CancelApproachDeleting -> dispatch(
                    Msg.ApproachDeletingFinished(intent.approachId)
                )
                is ScheduleStore.Intent.RequestExerciseDeleting -> requestExerciseDeleting(
                    exerciseId = intent.exerciseId,
                    getState = getState,
                )
                is ScheduleStore.Intent.CancelExerciseDeleting -> dispatch(
                    Msg.ExerciseDeletingFinished(intent.exerciseId)
                )
                is ScheduleStore.Intent.ChangeApproachesCount -> dispatch(
                    Msg.ApproachesCountChanged(intent.approachesCount)
                )
                is ScheduleStore.Intent.ChangeExerciseName -> dispatch(
                    Msg.ExerciseNameChanged(intent.name)
                )
                is ScheduleStore.Intent.ChangeRepetitionsCount -> dispatch(
                    Msg.RepetitionsCountChanged(intent.repetitionsCount)
                )
                is ScheduleStore.Intent.ChangeWeight -> dispatch(
                    Msg.WeightChanged(intent.weight),
                )
            }

        private fun addExercise(
            getState: () -> ScheduleStore.State,
        ) {
            val state = getState()
            state.trainingProgram?.let { trainingProgram ->
                database
                    .addExercise(
                        trainingId = trainingProgram.training.id,
                        approachesCount = state.approachesCount,
                        weight = state.weight,
                        repetitionsCount = state.repetitionsCount,
                        exerciseTemplate = state.exerciseTemplates
                            .find { it.name == state.exerciseName }
                            ?.let {
                                NewOrExistingExerciseTemplate.ExistingExerciseTemplate(
                                    id = it.id
                                )
                            } ?: NewOrExistingExerciseTemplate.NewExerciseTemplate(
                                name = state.exerciseName,
                            )
                    )
                    .subscribeScoped()
            }
        }

        private fun createNewTrainingProgram() {
            database
                .createAndSetEmptyProgram()
                .subscribeScoped()
        }

        private fun changeTrainingProgram(
            trainingProgramId: Long,
        ) {
            database
                .setProgram(trainingProgramId)
                .subscribeScoped()
        }

        private fun addApproach(exerciseId: Long) {
            database.addApproach(
                exerciseId = exerciseId
            )
                .subscribeScoped()
        }

        private fun changeWeight(
            approachId: Long,
            weight: Float,
        ) {
            database.updateWeight(
                approachId = approachId,
                weight = weight,
            )
                .subscribeScoped()
        }

        private fun changeRepetitions(
            approachId: Long,
            repetitions: Int,
        ) {
            database.updateRepetitions(
                approachId = approachId,
                repetitions = repetitions,
            )
                .subscribeScoped()
        }

        private fun changeTrainingProgramName(
            name: String,
            getState: () -> ScheduleStore.State,
        ) {
            dispatch(Msg.TrainingProgramNameChanged(name))
            getState().trainingProgram?.let { trainingProgram ->
               database
                   .updateTrainingProgramName(
                       id = trainingProgram.id,
                       name = name,
                   )
                   .subscribeScoped()
           }
        }

        private fun requestApproachDeleting(
            approachId: Long,
            getState: () -> ScheduleStore.State,
        ) {
            dispatch(Msg.ApproachDeletingRequested(approachId))
            completableTimer(4500, ioScheduler) // This delay is need to wait for possible deleting cancellation
                .andThen (
                    completableDefer {
                        if (approachId in getState().deleteApproachRequests) {
                            database
                                .deleteApproach(approachId)
                                .delay(500, ioScheduler) // This delay is need to wait for approaches list update
                                .doOnAfterComplete {
                                    mainScheduler.newExecutor().submit {
                                        dispatch(Msg.ApproachDeletingFinished(approachId))
                                    }
                                }
                        } else {
                            completableOfEmpty()
                        }
                    }
                )
                .subscribeOn(ioScheduler)
                .subscribeScoped()

//            completableDefer {
//                if (approachId in getState().deleteApproachRequests) {
//                    CoroutineScope(Dispatchers.Main).launch {
//
//                    }
//                    database.deleteApproach(
//                        approachId = approachId,
//                    )
//                }
//                else {
//                    completableOfEmpty()
//                }
//            }
//                .subscribeOn(ioScheduler)
//                .subscribeScoped()

        }

        private fun requestExerciseDeleting(
            exerciseId: Long,
            getState: () -> ScheduleStore.State,
        ) {
            dispatch(Msg.ExerciseDeletingRequested(exerciseId))
            completableTimer(5000, ioScheduler) // This delay is need to wait for possible deleting cancellation
                .andThen (
                    completableDefer {
                        if (exerciseId in getState().deleteExerciseRequests) {
                            database
                                .deleteExercise(exerciseId)
                                .delay(500, ioScheduler) // This delay is need to wait for approaches list update
                                .doOnAfterComplete {
                                    mainScheduler.newExecutor().submit {
                                        dispatch(Msg.ExerciseDeletingFinished(exerciseId))
                                    }
                                }
                        } else {
                            completableOfEmpty()
                        }
                    }
                )
                .subscribeOn(ioScheduler)
                .subscribeScoped()
        }
    }

    private object ReducerImpl : Reducer<ScheduleStore.State, Msg> {
        override fun ScheduleStore.State.reduce(msg: Msg): ScheduleStore.State =
            when (msg) {
                is Msg.TrainingProgramLoaded -> copy(
                        trainingProgram = msg.trainingProgram,
                    )
                is Msg.TrainingProgramsShortLoaded -> copy(
                        trainingProgramsShort = msg.trainingProgramsShort,
                    )
                is Msg.ExerciseTemplatesLoaded -> copy(
                    exerciseTemplates = msg.exerciseTemplates,
                )
                is Msg.ExerciseNameChanged -> copy(
                    exerciseName = msg.exerciseName,
                )
                is Msg.ApproachesCountChanged -> copy(
                    approachesCount = msg.approachesCount,
                )
                is Msg.RepetitionsCountChanged -> copy(
                    repetitionsCount = msg.repetitionsCount,
                )
                is Msg.WeightChanged -> copy(
                    weight = msg.weight,
                )
                is Msg.TrainingProgramNameChanged -> copy(
                    trainingProgram = trainingProgram?.copy(
                        name = msg.name,
                    )
                )
                is Msg.ApproachDeletingRequested -> copy(
                    deleteApproachRequests = deleteApproachRequests.plus(msg.approachId)
                )
                is Msg.ExerciseDeletingRequested -> copy(
                    deleteExerciseRequests = deleteExerciseRequests.plus(msg.exerciseId)
                )
                is Msg.ApproachDeletingFinished -> copy(
                    deleteApproachRequests = deleteApproachRequests.minus(msg.approachId)
                )
                is Msg.ExerciseDeletingFinished -> copy(
                    deleteExerciseRequests = deleteExerciseRequests.minus(msg.exerciseId)
                )
//                is Msg.ApproachDeleted -> copy(
//                    trainingProgram = trainingProgram?.copy(
//                        training = trainingProgram.training.copy(
//                            exercises =
//                                    trainingProgram.training.exercises
//                                        .find { it.id == msg.exerciseId }
//                                        ?.let { exercise ->
//                                            trainingProgram.training.exercises
//                                                .dropWhile { it.id == msg.exerciseId }
//                                                .plus(
//                                                    exercise.copy(
//                                                        approaches = exercise.approaches
//                                                            .dropWhile { it.id == msg.approachId }
//                                                    )
//                                                )
//                                        } ?: trainingProgram.training.exercises
//                        )
//                    )
//                )
            }
    }
}
