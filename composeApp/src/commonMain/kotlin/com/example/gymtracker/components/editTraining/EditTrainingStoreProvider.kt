package com.example.gymtracker.components.editTraining

import androidx.lifecycle.AtomicReference
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.andThen
import com.badoo.reaktive.completable.completableDefer
import com.badoo.reaktive.completable.completableOfEmpty
import com.badoo.reaktive.completable.completableTimer
import com.badoo.reaktive.completable.delay
import com.badoo.reaktive.completable.doOnAfterComplete
import com.badoo.reaktive.completable.subscribeOn
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.example.gymtracker.database.databases.EditTrainingDatabase
import com.example.gymtracker.database.databases.NewOrExistingExerciseTemplate
import com.example.gymtracker.domain.CompletedTraining
import com.example.gymtracker.domain.ExerciseTemplate
import com.example.gymtracker.utils.add
import com.example.gymtracker.utils.remove

internal class EditTrainingStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: EditTrainingDatabase,
    private val completedTrainingId: Long,
) {
    fun provide(): EditTrainingStore =
        object :
            EditTrainingStore,
            Store<EditTrainingStore.Intent, EditTrainingStore.State, Nothing> by storeFactory.create(
                name = "EditTrainingStore",
                initialState = EditTrainingStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class CompletedTrainingLoaded(
            val completedTraining: CompletedTraining?,
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

        data class CompletedTrainingNameChanged(
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
    }

    private inner class ExecutorImpl : ReaktiveExecutor<EditTrainingStore.Intent, Unit, EditTrainingStore.State, Msg, Nothing>() {
        private val pendingExerciseDeletions = AtomicReference(emptyMap<Long, Disposable>())
        private val pendingApproachDeletions = AtomicReference(emptyMap<Long, Disposable>())

        override fun executeAction(
            action: Unit,
            getState: () -> EditTrainingStore.State,
        ) {
            database
                .observeCompletedTraining(completedTrainingId)
                .observeOn(mainScheduler)
                .map(Msg::CompletedTrainingLoaded)
                .subscribeScoped(
                    onNext = { completedTrainingLoadedMessage ->
                        dispatch(completedTrainingLoadedMessage)
                    },
                )

            database
                .observeExerciseTemplateList()
                .observeOn(mainScheduler)
                .map(Msg::ExerciseTemplatesLoaded)
                .subscribeScoped(
                    onNext = { exerciseTemplatesLoadedMessage ->
                        dispatch(exerciseTemplatesLoadedMessage)
                    },
                )
        }

        override fun executeIntent(
            intent: EditTrainingStore.Intent,
            getState: () -> EditTrainingStore.State,
        ) = when (intent) {
            is EditTrainingStore.Intent.AddExercise ->
                addExercise(
                    getState = getState,
                )
            is EditTrainingStore.Intent.AddApproach ->
                addApproach(
                    exerciseId = intent.exerciseId,
                )
            is EditTrainingStore.Intent.ChangeApproachWeight ->
                changeWeight(
                    approachId = intent.approachId,
                    weight = intent.weight,
                )
            is EditTrainingStore.Intent.ChangeApproachRepetitions ->
                changeRepetitions(
                    approachId = intent.approachId,
                    repetitions = intent.repetitions,
                )
            is EditTrainingStore.Intent.ChangeCompletedTrainingName ->
                changeCompletedTrainingName(
                    name = intent.name,
                )
            is EditTrainingStore.Intent.RequestApproachDeleting ->
                requestApproachDeleting(
                    approachId = intent.approachId,
                    getState = getState,
                )
            is EditTrainingStore.Intent.CancelApproachDeleting ->
                dispatch(
                    Msg.ApproachDeletingFinished(intent.approachId),
                )
            is EditTrainingStore.Intent.RequestExerciseDeleting ->
                requestExerciseDeleting(
                    exerciseId = intent.exerciseId,
                    getState = getState,
                )
            is EditTrainingStore.Intent.CancelExerciseDeleting ->
                dispatch(
                    Msg.ExerciseDeletingFinished(intent.exerciseId),
                )
            is EditTrainingStore.Intent.ChangeApproachesCount ->
                dispatch(
                    Msg.ApproachesCountChanged(intent.approachesCount),
                )
            is EditTrainingStore.Intent.ChangeExerciseName ->
                dispatch(
                    Msg.ExerciseNameChanged(intent.name),
                )
            is EditTrainingStore.Intent.ChangeRepetitionsCount ->
                dispatch(
                    Msg.RepetitionsCountChanged(intent.repetitionsCount),
                )
            is EditTrainingStore.Intent.ChangeWeight ->
                dispatch(
                    Msg.WeightChanged(intent.weight),
                )
        }

        private fun addExercise(getState: () -> EditTrainingStore.State) {
            val state = getState()
            state.completedTraining?.let { completedTraining ->
                database
                    .addExercise(
                        trainingId = completedTraining.training.id,
                        approachesCount = state.approachesCount,
                        weight = state.weight,
                        repetitionsCount = state.repetitionsCount,
                        exerciseTemplate =
                            state.exerciseTemplates
                                .find { it.name == state.exerciseName }
                                ?.let {
                                    NewOrExistingExerciseTemplate.ExistingExerciseTemplate(
                                        id = it.id,
                                    )
                                } ?: NewOrExistingExerciseTemplate.NewExerciseTemplate(
                                name = state.exerciseName,
                            ),
                    )
                    .subscribeScoped()
            }
        }

        private fun addApproach(exerciseId: Long) {
            database.addApproach(
                exerciseId = exerciseId,
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

        private fun changeCompletedTrainingName(name: String) {
            dispatch(Msg.CompletedTrainingNameChanged(name))
            database
                .updateCompletedTrainingName(
                    name = name,
                )
                .subscribeScoped()
        }

        private fun requestApproachDeleting(
            approachId: Long,
            getState: () -> EditTrainingStore.State,
        ) {
            pendingApproachDeletions.remove(approachId)
            dispatch(Msg.ApproachDeletingRequested(approachId))
            pendingApproachDeletions.add(
                key = approachId,
                value =
                    completableTimer(4500, ioScheduler) // This delay is need to wait for possible deleting cancellation
                        .andThen(
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
                            },
                        )
                        .subscribeOn(ioScheduler)
                        .subscribeScoped(),
            )
        }

        private fun requestExerciseDeleting(
            exerciseId: Long,
            getState: () -> EditTrainingStore.State,
        ) {
            pendingExerciseDeletions.remove(exerciseId)
            dispatch(Msg.ExerciseDeletingRequested(exerciseId))
            pendingExerciseDeletions.add(
                key = exerciseId,
                value =
                    completableTimer(5000, ioScheduler) // This delay is need to wait for possible deleting cancellation
                        .andThen(
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
                            },
                        )
                        .subscribeOn(ioScheduler)
                        .subscribeScoped(),
            )
        }
    }

    private object ReducerImpl :
        Reducer<EditTrainingStore.State, Msg> {
        override fun EditTrainingStore.State.reduce(msg: Msg): EditTrainingStore.State =
            when (msg) {
                is Msg.CompletedTrainingLoaded ->
                    copy(
                        completedTraining = msg.completedTraining,
                    )
                is Msg.ExerciseTemplatesLoaded ->
                    copy(
                        exerciseTemplates = msg.exerciseTemplates,
                    )
                is Msg.ExerciseNameChanged ->
                    copy(
                        exerciseName = msg.exerciseName,
                    )
                is Msg.ApproachesCountChanged ->
                    copy(
                        approachesCount = msg.approachesCount,
                    )
                is Msg.RepetitionsCountChanged ->
                    copy(
                        repetitionsCount = msg.repetitionsCount,
                    )
                is Msg.WeightChanged ->
                    copy(
                        weight = msg.weight,
                    )
                is Msg.CompletedTrainingNameChanged ->
                    copy(
                        completedTraining =
                            completedTraining?.copy(
                                name = msg.name,
                            ),
                    )
                is Msg.ApproachDeletingRequested ->
                    copy(
                        deleteApproachRequests = deleteApproachRequests.plus(msg.approachId),
                    )
                is Msg.ExerciseDeletingRequested ->
                    copy(
                        deleteExerciseRequests = deleteExerciseRequests.plus(msg.exerciseId),
                    )
                is Msg.ApproachDeletingFinished ->
                    copy(
                        deleteApproachRequests = deleteApproachRequests.minus(msg.approachId),
                    )
                is Msg.ExerciseDeletingFinished ->
                    copy(
                        deleteExerciseRequests = deleteExerciseRequests.minus(msg.exerciseId),
                    )
            }
    }
}
