package com.example.gymtracker.routing

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.gymtracker.components.editTraining.EditTrainingComponent
import com.example.gymtracker.components.history.HistoryComponent
import com.example.gymtracker.components.currentTraining.CurrentTrainingComponent
import com.example.gymtracker.components.schedule.ScheduleComponent
import com.example.gymtracker.database.DatabasesBuilder
import com.example.gymtracker.utils.Consumer
import com.example.gymtracker.utils.now
import com.example.gymtracker.utils.russianName
import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

class RootRouter(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    val databasesBuilder: DatabasesBuilder,
) : ComponentContext by componentContext {
    companion object {
        private fun generateCurrentTrainingScreenTitle() =
            "Текущая тренировка, ${LocalDate.now().dayOfWeek.russianName()}"
    }

    data class Model(
        val screenTitle: String = "",
        val isCurrentTrainingScreenActive: Boolean = false,
        val isScheduleScreenActive: Boolean = false,
        val isHistoryScreenActive: Boolean = false,
        val isEditTrainingScreenActive: Boolean = false,
    )

    val model: MutableValue<Model> = MutableValue(Model())

    @Serializable
    private sealed class ScreenConfig {
        @Serializable
        data object CurrentTraining : ScreenConfig()

        @Serializable
        data class Schedule(val dayOfWeek: DayOfWeek) : ScreenConfig()

        @Serializable
        data object History : ScreenConfig()

        @Serializable
        data class EditTraining(val trainingId: Long) : ScreenConfig()
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
        stack.subscribe {
            when (it.active.configuration) {
                ScreenConfig.CurrentTraining ->
                    model.update {
                        Model(
                            isCurrentTrainingScreenActive = true,
                            screenTitle = generateCurrentTrainingScreenTitle(),
                        )
                    }
                is ScreenConfig.EditTraining ->
                    model.update {
                        Model(
                            isEditTrainingScreenActive = true,
                            screenTitle = "Изменить тренировку",
                        )
                    }
                ScreenConfig.History ->
                    model.update {
                        Model(
                            isHistoryScreenActive = true,
                            screenTitle = "История тренировок",
                        )
                    }
                is ScreenConfig.Schedule ->
                    model.update {
                        Model(
                            isScheduleScreenActive = true,
                            screenTitle = "Изменить расписание",
                        )
                    }
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
                    ),
                )

            is ScreenConfig.Schedule -> Child.Schedule(
                ScheduleComponent(
                    componentContext = componentContext,
                    storeFactory = storeFactory,
                    database = databasesBuilder.createScheduleDatabase(
                        dayOfWeek = screenConfig.dayOfWeek
                    )
                )
            )

            is ScreenConfig.History ->
                Child.History(
                    HistoryComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        output = Consumer(::onHistoryOutput),
                    ),
                )

            is ScreenConfig.EditTraining ->
                Child.EditTraining(
                    EditTrainingComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        trainingId = screenConfig.trainingId,
                    ),
                )
        }

    fun onBackClicked() {
        router.pop()
    }

    fun onHistoryOutput(output: HistoryComponent.Output) =
        when (output) {
            is HistoryComponent.Output.EditTrainingTransit -> {
                router.push(
                    ScreenConfig.EditTraining(
                        trainingId = output.trainingId,
                    ),
                )
            }
        }

    fun onCurrentTrainingScreenMenuButtonClicked() {
        router.navigate { stack ->
            stack
                .find { it == ScreenConfig.CurrentTraining }
                ?.let { currentTrainingScreenConfig ->
                    stack
                        .filter { it != ScreenConfig.CurrentTraining }
                        .plus(currentTrainingScreenConfig)
                } ?: (stack + ScreenConfig.CurrentTraining)
        }
    }

    fun onScheduleScreenMenuButtonClicked() {
        router.navigate { stack ->
            stack
                .find { it is ScreenConfig.Schedule }
                ?.let { scheduleScreenConfig ->
                    stack
                        .filterNot { it is ScreenConfig.Schedule }
                        .plus(scheduleScreenConfig)
                } ?: (stack + ScreenConfig.Schedule(LocalDate.now().dayOfWeek))
        }
    }

    fun onWeekdaySwitch(dayOfWeek: DayOfWeek) {
        router.navigate { stack ->
            stack
                .filterNot { it is ScreenConfig.Schedule }
                .plus(ScreenConfig.Schedule(dayOfWeek))
        }
    }

    fun onHistoryScreenMenuButtonClicked() {
        router.navigate { stack ->
            stack
                .find { it == ScreenConfig.History }
                ?.let { historyScreenConfig ->
                    stack
                        .filter { it != ScreenConfig.History }
                        .plus(historyScreenConfig)
                } ?: (stack + ScreenConfig.History)
        }
    }
}
