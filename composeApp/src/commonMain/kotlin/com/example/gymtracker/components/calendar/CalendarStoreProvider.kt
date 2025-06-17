package com.example.gymtracker.components.calendar

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import com.example.gymtracker.database.databases.CalendarDatabase
import com.example.gymtracker.domain.CompletedTrainingShort
import com.example.gymtracker.domain.CompletedTrainingTitle

internal class CalendarStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: CalendarDatabase,
) {
    fun provide(): CalendarStore =
        object :
            CalendarStore,
            Store<CalendarStore.Intent, CalendarStore.State, Nothing> by storeFactory.create(
                name = "CalendarStore",
                initialState = CalendarStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class CompletedTrainingsLoaded(
            val completedTrainings: List<CompletedTrainingShort>,
        ) : Msg()

        data class CompletedTrainingTitlesLoaded(
            val completedTrainingTitles: List<CompletedTrainingTitle>,
        ) : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<CalendarStore.Intent, Unit, CalendarStore.State, Msg, Nothing>() {
        override fun executeAction(
            action: Unit,
            getState: () -> CalendarStore.State,
        ) {
            database
                .observeCompletedTrainings()
                .observeOn(mainScheduler)
                .map(Msg::CompletedTrainingsLoaded)
                .subscribeScoped(
                    onNext = { completedTrainingsLoadedMessage ->
                        dispatch(completedTrainingsLoadedMessage)
                    },
                )

            database
                .observeCompletedTrainingTitles()
                .observeOn(mainScheduler)
                .map(Msg::CompletedTrainingTitlesLoaded)
                .subscribeScoped(
                    onNext = { completedTrainingTitlesLoadedMessage ->
                        dispatch(completedTrainingTitlesLoadedMessage)
                    },
                )
        }
    }

    private object ReducerImpl :
        Reducer<CalendarStore.State, Msg> {
        override fun CalendarStore.State.reduce(msg: Msg): CalendarStore.State =
            when (msg) {
                is Msg.CompletedTrainingsLoaded ->
                    copy(
                        completedTrainings = msg.completedTrainings,
                    )
                is Msg.CompletedTrainingTitlesLoaded ->
                    copy(
                        completedTrainingTitles = msg.completedTrainingTitles,
                    )
            }
    }
}
