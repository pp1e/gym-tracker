package com.example.gymtracker.components.history

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import com.example.gymtracker.database.databases.HistoryDatabase
import com.example.gymtracker.domain.CompletedTrainingShort

internal class HistoryStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: HistoryDatabase,
) {
    fun provide(): HistoryStore =
        object :
            HistoryStore,
            Store<HistoryStore.Intent, HistoryStore.State, Nothing> by storeFactory.create(
                name = "HistoryStore",
                initialState = HistoryStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class CompletedTrainingsLoaded(
            val completedTrainings: List<CompletedTrainingShort>,
        ) : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<HistoryStore.Intent, Unit, HistoryStore.State, Msg, Nothing>() {
        override fun executeAction(
            action: Unit,
            getState: () -> HistoryStore.State,
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
        }
    }

    private object ReducerImpl :
        Reducer<HistoryStore.State, Msg> {
        //        override fun MainStore.State.reduce(msg: Msg): MainStore.State =
//            when (msg) {
//                is Msg.ExistCategoriesLoaded -> copy(
//                    existCategories = msg.existCategories,
//                    category = msg.categoryId?.let { categoryId ->
//                        msg.existCategories.find { it.id == categoryId }?.name
//                    } ?: category
//                )
//                is Msg.CategoryChanged -> copy(category = msg.category)
//                is Msg.TypeChanged -> copy(type = msg.type)
//                is Msg.AmountChanged -> copy(amount = msg.amount)
//                is Msg.MessageChanged -> copy(message = msg.message)
//            }
        override fun HistoryStore.State.reduce(msg: Msg): HistoryStore.State =
            when (msg) {
                is Msg.CompletedTrainingsLoaded ->
                    copy(
                        completedTrainings = msg.completedTrainings,
                    )
            }
    }
}
