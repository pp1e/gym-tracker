package com.example.gymtracker.components.history

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import com.example.gymtracker.database.databases.HistoryDatabase
import com.example.gymtracker.domain.CompleteTraining
import com.example.gymtracker.utils.asValue

class HistoryComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: HistoryDatabase,
    private val output: Consumer<Output>,
) : ComponentContext by componentContext {
    data class Model (
        val completeTrainings: List<CompleteTraining> = emptyList(),
    )

    sealed class Output {
        data class EditTrainingTransit(val trainingId: Long) : Output()
    }

    private val store =
        instanceKeeper.getStore {
            HistoryStoreProvider(
                storeFactory = storeFactory,
                database = database,
            ).provide()
        }

    val model: Value<Model> = store.asValue(::stateToModel)

    fun onTrainingClicked(trainingId: Long) {
        output(Output.EditTrainingTransit(trainingId))
    }
}
