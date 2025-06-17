package com.example.gymtracker.routing

import com.example.gymtracker.components.calendar.CalendarComponent
import com.example.gymtracker.components.currentTraining.CurrentTrainingComponent
import com.example.gymtracker.components.editTraining.EditTrainingComponent
import com.example.gymtracker.components.history.HistoryComponent
import com.example.gymtracker.components.schedule.ScheduleComponent

sealed class Child {
    data class CurrentTraining(val component: CurrentTrainingComponent) : Child()

    data class Schedule(val component: ScheduleComponent) : Child()

    data class History(val component: HistoryComponent) : Child()

    data class EditTraining(val component: EditTrainingComponent) : Child()

    data class Calendar(val component: CalendarComponent) : Child()

    data object Profile: Child()
}
