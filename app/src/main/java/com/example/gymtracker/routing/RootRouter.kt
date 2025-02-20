package com.example.gymtracker.routing

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.gymtracker.components.main.MainComponent
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class RootRouter(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
//    database: AppRepository
) : ComponentContext by componentContext {
    companion object {
        private val dayOfWeekFormatter =
            DateTimeFormatter.ofPattern("EEEE", Locale("ru", "RU"))

        private fun  generateTrainingScreenTitle() =
            "Текущая тренировка, ${LocalDate.now().format(dayOfWeekFormatter)}"
    }

    data class Model(
        val screenTitle: String = generateTrainingScreenTitle(),
        val isTrainingScreenActive: Boolean = true,
        val isScheduleScreenActive: Boolean = false,
        val isHistoryScreenActive: Boolean = false,
    )

    val model: MutableValue<Model> = MutableValue(Model())

    @Serializable
    private sealed class ScreenConfig {
        @Serializable
        data object Training : ScreenConfig()

        @Serializable
        data object Schedule : ScreenConfig()

        @Serializable
        data object History : ScreenConfig()
    }

    private val router = StackNavigation<ScreenConfig>()

    private val stack =
        childStack(
            source = router,
            initialConfiguration = ScreenConfig.Training,
            handleBackButton = true,
            childFactory = ::createChild,
            serializer = ScreenConfig.serializer()
        )

    val childStack: Value<ChildStack<*, Child>> = stack

    private fun createChild(
        screenConfig: ScreenConfig,
        componentContext: ComponentContext
    ): Child =
        when (screenConfig) {
            is ScreenConfig.Training -> Child.Training(
                MainComponent(
                    componentContext = componentContext,
                    storeFactory = storeFactory,
//                    Consumer(::onMainOutput)
                )
            )

            is ScreenConfig.Schedule -> Child.Schedule

            is ScreenConfig.History -> Child.History
        }

    fun onTrainingScreenMenuButtonClicked() {
        router.navigate { listOf(ScreenConfig.Training) }
        model.update {
            it.copy(
                isTrainingScreenActive = true,
                isScheduleScreenActive = false,
                isHistoryScreenActive = false,
                screenTitle = generateTrainingScreenTitle(),
            )
        }
    }

    fun onScheduleScreenMenuButtonClicked() {
        router.navigate { listOf(ScreenConfig.Schedule) }
        model.update {
            it.copy(
                isTrainingScreenActive = false,
                isScheduleScreenActive = true,
                isHistoryScreenActive = false,
                screenTitle = "Изменить расписание",
            )
        }
    }

    fun onHistoryScreenMenuButtonClicked() {
        router.navigate { listOf(ScreenConfig.History) }
        model.update {
            it.copy(
                isTrainingScreenActive = false,
                isScheduleScreenActive = false,
                isHistoryScreenActive = true,
                screenTitle = "История тренировок",
            )
        }
    }
}
