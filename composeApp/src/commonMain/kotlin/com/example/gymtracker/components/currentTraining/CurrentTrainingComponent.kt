package com.example.gymtracker.components.currentTraining

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.gymtracker.database.databases.CurrentTrainingDatabase
import com.example.gymtracker.domain.Training
import com.example.gymtracker.utils.asValue

class CurrentTrainingComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: CurrentTrainingDatabase,
//    private val output: Consumer<Output>
) : ComponentContext by componentContext {
    data class Model(
        val training: Training? = null,
    )

    object Output

    private val store =
        instanceKeeper.getStore {
            CurrentTrainingStoreProvider(
                storeFactory = storeFactory,
                database = database
            ).provide()
        }

    val model: Value<Model> = store.asValue(::stateToModel)
}
