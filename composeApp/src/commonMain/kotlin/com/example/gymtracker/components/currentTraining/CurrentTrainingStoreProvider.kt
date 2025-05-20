package com.example.gymtracker.components.currentTraining

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
import com.example.gymtracker.database.databases.CurrentTrainingDatabase
import com.example.gymtracker.database.databases.NewOrExistingExerciseTemplate
import com.example.gymtracker.domain.CurrentTraining
import com.example.gymtracker.domain.ExerciseTemplate
import com.example.gymtracker.domain.TrainingProgramShort
import com.example.gymtracker.utils.add
import com.example.gymtracker.utils.remove
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

internal class CurrentTrainingStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: CurrentTrainingDatabase,
) {
    fun provide(): CurrentTrainingStore =
        object :
            CurrentTrainingStore,
            Store<CurrentTrainingStore.Intent, CurrentTrainingStore.State, Nothing> by storeFactory.create(
                name = "CurrentTrainingStore",
                initialState = CurrentTrainingStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class CurrentTrainingLoaded(val currentTraining: CurrentTraining?) : Msg()

        data class TrainingProgramsShortLoaded(
            val trainingProgramsShort: List<TrainingProgramShort>,
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

        data class CurrentTrainingNameChanged(
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

    private inner class ExecutorImpl : ReaktiveExecutor<CurrentTrainingStore.Intent, Unit, CurrentTrainingStore.State, Msg, Nothing>() {
        private val pendingExerciseDeletions = AtomicReference(emptyMap<Long, Disposable>())
        private val pendingApproachDeletions = AtomicReference(emptyMap<Long, Disposable>())

        override fun executeAction(
            action: Unit,
            getState: () -> CurrentTrainingStore.State,
        ) {
            database
                .observeCurrentTraining()
                .observeOn(mainScheduler)
                .map(Msg::CurrentTrainingLoaded)
                .subscribeScoped(
                    onNext = { trainingLoadedMessage ->
                        dispatch(trainingLoadedMessage)
                    },
                )

            database
                .observeTrainingProgramList()
                .observeOn(mainScheduler)
                .map(Msg::TrainingProgramsShortLoaded)
                .subscribeScoped(
                    onNext = { trainingProgramsShortLoadedMessage ->
                        dispatch(trainingProgramsShortLoadedMessage)
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
            intent: CurrentTrainingStore.Intent,
            getState: () -> CurrentTrainingStore.State,
        ) = when (intent) {
            is CurrentTrainingStore.Intent.StartTraining -> startTraining()
            is CurrentTrainingStore.Intent.DeleteTraining -> deleteTraining()
            is CurrentTrainingStore.Intent.ChangeTrainingProgram ->
                changeTrainingProgram(
                    trainingProgramId = intent.trainingProgramId,
                )
            is CurrentTrainingStore.Intent.AddExercise ->
                addExercise(
                    getState = getState,
                )
            is CurrentTrainingStore.Intent.AddApproach ->
                addApproach(
                    exerciseId = intent.exerciseId,
                )
            is CurrentTrainingStore.Intent.ChangeApproachWeight ->
                changeWeight(
                    approachId = intent.approachId,
                    weight = intent.weight,
                )
            is CurrentTrainingStore.Intent.ChangeApproachRepetitions ->
                changeRepetitions(
                    approachId = intent.approachId,
                    repetitions = intent.repetitions,
                )
            is CurrentTrainingStore.Intent.ChangeCurrentTrainingName ->
                changeCurrentTrainingName(
                    name = intent.name,
                )
            is CurrentTrainingStore.Intent.RequestApproachDeleting ->
                requestApproachDeleting(
                    approachId = intent.approachId,
                    getState = getState,
                )
            is CurrentTrainingStore.Intent.CancelApproachDeleting ->
                dispatch(
                    Msg.ApproachDeletingFinished(intent.approachId),
                )
            is CurrentTrainingStore.Intent.RequestExerciseDeleting ->
                requestExerciseDeleting(
                    exerciseId = intent.exerciseId,
                    getState = getState,
                )
            is CurrentTrainingStore.Intent.CancelExerciseDeleting ->
                dispatch(
                    Msg.ExerciseDeletingFinished(intent.exerciseId),
                )
            is CurrentTrainingStore.Intent.ChangeApproachesCount ->
                dispatch(
                    Msg.ApproachesCountChanged(intent.approachesCount),
                )
            is CurrentTrainingStore.Intent.ChangeExerciseName ->
                dispatch(
                    Msg.ExerciseNameChanged(intent.name),
                )
            is CurrentTrainingStore.Intent.ChangeRepetitionsCount ->
                dispatch(
                    Msg.RepetitionsCountChanged(intent.repetitionsCount),
                )
            is CurrentTrainingStore.Intent.ChangeWeight ->
                dispatch(
                    Msg.WeightChanged(intent.weight),
                )
            is CurrentTrainingStore.Intent.SaveTrainingToHistory -> saveTrainingToHistory(getState)
            is CurrentTrainingStore.Intent.UpdateStartedAt -> updateStartedAt(intent.startedAt)
        }

        private fun addExercise(getState: () -> CurrentTrainingStore.State) {
            val state = getState()
            state.currentTraining?.let { currentTraining ->
                database
                    .addExercise(
                        trainingId = currentTraining.training.id,
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

        private fun deleteTraining() {
            database
                .deleteTraining()
                .subscribeScoped()
        }

        private fun startTraining() {
            database
                .startTraining()
                .subscribeScoped()
        }

        private fun changeTrainingProgram(trainingProgramId: Long) {
            database
                .setTrainingByProgram(trainingProgramId)
                .subscribeScoped()
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

        private fun changeCurrentTrainingName(name: String) {
            dispatch(Msg.CurrentTrainingNameChanged(name))
            database
                .updateCurrentTrainingName(
                    name = name,
                )
                .subscribeScoped()
        }

        private fun requestApproachDeleting(
            approachId: Long,
            getState: () -> CurrentTrainingStore.State,
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
            getState: () -> CurrentTrainingStore.State,
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

        private fun saveTrainingToHistory(getState: () -> CurrentTrainingStore.State) {
            getState().currentTraining?.let { currentTraining ->
                database.moveTrainingToHistory(
                    name = currentTraining.name,
                    trainingId = currentTraining.training.id,
                    startedAt = currentTraining.startedAt,
                )
                    .subscribeScoped()
            }
        }

        private fun updateStartedAt(startedAt: LocalDateTime) {
            database
                .updateStartedAt(startedAt)
                .subscribeScoped()
        }
    }

    private object ReducerImpl : Reducer<CurrentTrainingStore.State, Msg> {
        override fun CurrentTrainingStore.State.reduce(msg: Msg): CurrentTrainingStore.State =
            when (msg) {
                is Msg.CurrentTrainingLoaded -> {
                    copy(
                        currentTraining = msg.currentTraining,
                        isTrainingIrrelevant =
                            msg.currentTraining?.let { currentTraining ->
                                Clock.System.now()
                                    .minus(
                                        currentTraining.startedAt
                                            .toInstant(
                                                TimeZone.currentSystemDefault(),
                                            ),
                                    )
                                    .inWholeHours > 8
                            } ?: false,
                    )
                }
                is Msg.TrainingProgramsShortLoaded ->
                    copy(
                        trainingProgramsShort = msg.trainingProgramsShort,
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
                is Msg.CurrentTrainingNameChanged ->
                    copy(
                        currentTraining =
                            currentTraining?.copy(
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
