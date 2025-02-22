package com.example.gymtracker.components.editTraining

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor

internal class EditTrainingStoreProvider(
    private val storeFactory: StoreFactory,
) {
    fun provide(): EditTrainingStore =
        object :
            EditTrainingStore,
            Store<EditTrainingStore.Intent, EditTrainingStore.State, Nothing> by storeFactory.create(
                name = "EditTrainingStore",
                initialState = EditTrainingStore.State,
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private object Msg

    private inner class ExecutorImpl : ReaktiveExecutor<EditTrainingStore.Intent, Unit, EditTrainingStore.State, Msg, Nothing>() {
//        override fun executeAction(action: Unit, getState: () -> MainStore.State) {
//        }
    }

    private object ReducerImpl :
        Reducer<EditTrainingStore.State, Msg> {
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
        override fun EditTrainingStore.State.reduce(msg: Msg): EditTrainingStore.State = EditTrainingStore.State
    }
}
