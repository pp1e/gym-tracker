package com.example.gymtracker.common.components.history

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor

internal class HistoryStoreProvider(
    private val storeFactory: StoreFactory,
) {
    fun provide(): HistoryStore =
        object :
            HistoryStore,
            Store<HistoryStore.Intent, HistoryStore.State, Nothing> by storeFactory.create(
                name = "HistoryStore",
                initialState = HistoryStore.State,
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private object Msg

    private inner class ExecutorImpl : ReaktiveExecutor<HistoryStore.Intent, Unit, HistoryStore.State, Msg, Nothing>() {
//        override fun executeAction(action: Unit, getState: () -> MainStore.State) {
//        }
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
        override fun HistoryStore.State.reduce(msg: Msg): HistoryStore.State = HistoryStore.State
    }
}