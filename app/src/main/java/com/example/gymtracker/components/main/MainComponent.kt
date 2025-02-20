package com.example.gymtracker.components.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.gymtracker.utils.asValue

class MainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
//    database: AppRepository,
//    private val output: Consumer<Output>
) : ComponentContext by componentContext {

    object Model

    object Output
    private val store = instanceKeeper.getStore {
        MainStoreProvider(
            storeFactory = storeFactory,
////                database = database
        ).provide()
    }

    val model: Value<Model> = store.asValue(::stateToModel)
}
