package com.example.gymtracker.routing

import com.example.gymtracker.components.editTraining.EditTrainingComponent
import com.example.gymtracker.components.history.HistoryComponent
import com.example.gymtracker.components.main.MainComponent

sealed class Child {
    data class CurrentTraining(val component: MainComponent) : Child()

    data object Schedule : Child()

    data class History(val component: HistoryComponent) : Child()

    data class EditTraining(val component: EditTrainingComponent) : Child()
}
