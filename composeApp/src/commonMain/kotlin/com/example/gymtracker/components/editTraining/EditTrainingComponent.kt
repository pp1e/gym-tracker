package com.example.gymtracker.components.editTraining

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.gymtracker.utils.asValue

class EditTrainingComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    trainingId: Long,
) : ComponentContext by componentContext {
    object Model

    object Output

    private val store =
        instanceKeeper.getStore {
            EditTrainingStoreProvider(
                storeFactory = storeFactory,
// //                database = database
            ).provide()
        }

    val model: Value<Model> = store.asValue(::stateToModel)
}
