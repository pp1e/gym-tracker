package com.example.gymtracker.components.calendar

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import com.example.gymtracker.components.history.CompletedTrainingMonth
import com.example.gymtracker.components.history.HistoryStoreProvider
import com.example.gymtracker.components.history.stateToModel
import com.example.gymtracker.database.databases.CalendarDatabase
import com.example.gymtracker.database.databases.HistoryDatabase
import com.example.gymtracker.domain.CompletedTrainingShort
import com.example.gymtracker.domain.CompletedTrainingTitle
import com.example.gymtracker.utils.asValue

class CalendarComponent (
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: CalendarDatabase,
    private val output: Consumer<Output>,
) : ComponentContext by componentContext {
    data class Model(
        val completedTrainings: List<CompletedTrainingShort>,
        val completedTrainingTitles: List<CompletedTrainingTitle>,
    )

    sealed class Output {
        data class EditTrainingTransit(val completedTrainingId: Long) : Output()
    }

    private val store =
        instanceKeeper.getStore {
            CalendarStoreProvider(
                storeFactory = storeFactory,
                database = database,
            ).provide()
        }

    val model: Value<Model> = store.asValue(::stateToModel)

    fun onTrainingClicked(trainingId: Long) {
        output(Output.EditTrainingTransit(trainingId))
    }
}