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
import com.example.gymtracker.components.main.MainComponent
import com.example.gymtracker.utils.Consumer
import com.example.gymtracker.utils.now
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat

class RootRouter(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
//    database: AppRepository
) : ComponentContext by componentContext {
    companion object {
        private fun generateCurrentTrainingScreenTitle() = "Текущая тренировка, ${LocalDate.now().dayOfWeek.name}"
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
        data object Schedule : ScreenConfig()

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
                ScreenConfig.Schedule ->
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
                    MainComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                    ),
                )

            is ScreenConfig.Schedule -> Child.Schedule

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

    fun onTrainingScreenMenuButtonClicked() {
        router.navigate { listOf(ScreenConfig.CurrentTraining) }
    }

    fun onScheduleScreenMenuButtonClicked() {
        router.navigate { listOf(ScreenConfig.Schedule) }
    }

    fun onHistoryScreenMenuButtonClicked() {
        router.navigate { listOf(ScreenConfig.History) }
    }
}
