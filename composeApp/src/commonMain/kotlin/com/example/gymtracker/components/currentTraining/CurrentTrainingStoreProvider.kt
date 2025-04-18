package com.example.gymtracker.components.currentTraining

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import com.example.gymtracker.components.entities.ApproachInsert
import com.example.gymtracker.components.entities.ExerciseInsert
import com.example.gymtracker.components.entities.TrainingInsert
import com.example.gymtracker.database.databases.CurrentTrainingDatabase
import com.example.gymtracker.domain.Training

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
        data class TrainingLoaded(val training: Training?) : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<CurrentTrainingStore.Intent, Unit, CurrentTrainingStore.State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> CurrentTrainingStore.State) {
            database
                .observeTraining()
                .observeOn(mainScheduler)
                .map(Msg::TrainingLoaded)
                .subscribeScoped(
                    onNext = { trainingLoadedMessage ->
                        dispatch(trainingLoadedMessage)
                    }
                )
        }

    }

    private object ReducerImpl : Reducer<CurrentTrainingStore.State, Msg> {
        override fun CurrentTrainingStore.State.reduce(msg: Msg): CurrentTrainingStore.State =
            when (msg) {
                is Msg.TrainingLoaded -> {
                    copy(
                        training = msg.training,
                    )
                }
            }
    }
}
