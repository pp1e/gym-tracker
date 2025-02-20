package com.example.gymtracker.components.main;

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor

internal class MainStoreProvider(
    private val storeFactory: StoreFactory,
//    private val database: AppRepository
) {
    fun provide(): MainStore =
        object :
            MainStore,
            Store<MainStore.Intent, MainStore.State, Nothing> by storeFactory.create(
                name = "MainStore",
                initialState = MainStore.State,
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private object Msg

    private inner class ExecutorImpl : ReaktiveExecutor<MainStore.Intent, Unit, MainStore.State, Msg, Nothing>() {
//        override fun executeAction(action: Unit, getState: () -> MainStore.State) {
//        }
    }


    private object ReducerImpl : Reducer<MainStore.State, Msg> {
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
        override fun MainStore.State.reduce(msg: Msg): MainStore.State = MainStore.State
    }
}
