package com.example.gymtracker.routing

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.StackNavigator
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.gymtracker.components.calendar.CalendarComponent
import com.example.gymtracker.components.currentTraining.CurrentTrainingComponent
import com.example.gymtracker.components.editTraining.EditTrainingComponent
import com.example.gymtracker.components.history.HistoryComponent
import com.example.gymtracker.components.schedule.ScheduleComponent
import com.example.gymtracker.database.DatabasesBuilder
import com.example.gymtracker.utils.Consumer
import com.example.gymtracker.utils.currentDayOfWeek
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable

class RootRouter(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    val databasesBuilder: DatabasesBuilder,
) : ComponentContext by componentContext {
    data class Model(
        val selectedWeekday: DayOfWeek = currentDayOfWeek(),
        val activeScreenConfig: ScreenConfig = ScreenConfig.CurrentTraining,
        val isTopBarExpanded: Boolean = false,
        val isCalendarButtonToggled: Boolean = false,
    )

    val model: MutableValue<Model> = MutableValue(Model())

    @Serializable
    sealed class ScreenConfig {
        @Serializable
        data object CurrentTraining : ScreenConfig()

        @Serializable
        data class Schedule(val dayOfWeek: DayOfWeek) : ScreenConfig()

        @Serializable
        data object History : ScreenConfig()

        @Serializable
        data class EditTraining(val completedTrainingId: Long) : ScreenConfig()

        @Serializable
        data object Calendar : ScreenConfig()
    }

    private val router = StackNavigation<ScreenConfig>()

    private val stack =
        childStack(
            source = router,
            initialConfiguration = ScreenConfig.CurrentTraining,
            handleBackButton = true,
            childFactory = ::createChild,
            serializer = ScreenConfig.serializer(),
        )

    val childStack: Value<ChildStack<*, Child>> = stack

    init {
        stack.subscribe { stack ->
            model.update {
                it.copy(
                    activeScreenConfig = stack.active.configuration,
                )
            }
        }
    }

    private fun createChild(
        screenConfig: ScreenConfig,
        componentContext: ComponentContext,
    ): Child =
        when (screenConfig) {
            is ScreenConfig.CurrentTraining ->
                Child.CurrentTraining(
                    CurrentTrainingComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        database = databasesBuilder.createCurrentTrainingDatabase(),
                        output = Consumer(::onCurrentTrainingOutput),
                    ),
                )

            is ScreenConfig.Schedule ->
                Child.Schedule(
                    ScheduleComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        database =
                            databasesBuilder.createScheduleDatabase(
                                dayOfWeek = screenConfig.dayOfWeek,
                            ),
                    ),
                )

            is ScreenConfig.History ->
                Child.History(
                    HistoryComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        database = databasesBuilder.createHistoryDatabase(),
                        output = Consumer(::onHistoryOutput),
                    ),
                )

            is ScreenConfig.EditTraining ->
                Child.EditTraining(
                    EditTrainingComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        completedTrainingId = screenConfig.completedTrainingId,
                        database = databasesBuilder.createEditTrainingDatabase(),
                        output = Consumer(::onEditTrainingOutput),
                    ),
                )

            is ScreenConfig.Calendar ->
                Child.Calendar(
                    CalendarComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        database = databasesBuilder.createCalendarDatabase(),
                        output = Consumer(::onCalendarOutput),
                    ),
                )
        }

    fun onBackClicked() {
        router.pop()
    }

    fun onCurrentTrainingOutput(output: CurrentTrainingComponent.Output) =
        when (output) {
            is CurrentTrainingComponent.Output.HistoryTransit -> {
                router.bringToFront(ScreenConfig.History)
            }
        }

    fun onHistoryOutput(output: HistoryComponent.Output) =
        when (output) {
            is HistoryComponent.Output.EditTrainingTransit -> {
                router.bringToFront(
                    ScreenConfig.EditTraining(
                        completedTrainingId = output.completedTrainingId,
                    ),
                )
            }
        }

    fun onCalendarOutput(output: CalendarComponent.Output) =
        when (output) {
            is CalendarComponent.Output.EditTrainingTransit -> {
                router.bringToFront(
                    ScreenConfig.EditTraining(
                        completedTrainingId = output.completedTrainingId,
                    ),
                )
            }
        }

    fun onEditTrainingOutput(output: EditTrainingComponent.Output) =
        when (output) {
            EditTrainingComponent.Output.HistoryTransit -> router.pop()
        }

    fun onCurrentTrainingScreenMenuButtonClick() {
        router.moveToFront(ScreenConfig.CurrentTraining)
    }

    fun onScheduleScreenMenuButtonClick() {
        router.moveToFront(
            ScreenConfig.Schedule(model.value.selectedWeekday),
        )
    }

    fun onWeekdaySwitch(dayOfWeek: DayOfWeek) {
        model.update {
            it.copy(
                selectedWeekday = dayOfWeek,
            )
        }
        router.bringToFront(
            ScreenConfig.Schedule(dayOfWeek),
        )
    }

    fun onHistoryScreenMenuButtonClick() {
        travelToHistory()
    }

    fun onCalenderButtonClick() {
        model.update {
            it.copy(
                isCalendarButtonToggled = !it.isCalendarButtonToggled,
            )
        }
        travelToHistory()
    }

    fun toggleTopBar() {
        model.update {
            it.copy(
                isTopBarExpanded = !it.isTopBarExpanded,
            )
        }
    }

    private fun travelToHistory() {
        if (model.value.isCalendarButtonToggled) {
            router.moveToFront(ScreenConfig.Calendar)
        } else {
            router.moveToFront(ScreenConfig.History)
        }
    }

    private fun <C : Any> StackNavigator<C>.moveToFront(newConfiguration: C) {
        this.navigate { stack ->
            stack
                .find { it::class == newConfiguration::class }
                ?.let { existentConfiguration ->
                    stack
                        .filterNot { it::class == newConfiguration::class }
                        .plus(existentConfiguration)
                } ?: (stack + newConfiguration)
        }
    }
}
