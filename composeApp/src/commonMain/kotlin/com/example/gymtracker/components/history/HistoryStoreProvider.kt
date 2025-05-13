package com.example.gymtracker.components.history

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import com.example.gymtracker.database.databases.HistoryDatabase
import com.example.gymtracker.domain.CompletedTrainingShort
import com.example.gymtracker.utils.now
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus

internal class HistoryStoreProvider(
    private val storeFactory: StoreFactory,
    private val database: HistoryDatabase,
) {
    fun provide(): HistoryStore =
        object :
            HistoryStore,
            Store<HistoryStore.Intent, HistoryStore.State, Nothing> by storeFactory.create(
                name = "HistoryStore",
                initialState = HistoryStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class CompletedTrainingMonthsLoaded(
            val completedTrainingMonths: List<CompletedTrainingMonth>,
        ) : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<HistoryStore.Intent, Unit, HistoryStore.State, Msg, Nothing>() {
        override fun executeAction(
            action: Unit,
            getState: () -> HistoryStore.State,
        ) {
            database
                .observeCompletedTrainings()
                .observeOn(mainScheduler)
                .map {
                    Msg.CompletedTrainingMonthsLoaded(
                        completedTrainingMonths = groupCompletedTrainingsByMonthAndWeek(
                            trainings = it,
                        )
                    )
                }
                .subscribeScoped(
                    onNext = { completedTrainingWeeksLoadedMessage ->
                        dispatch(completedTrainingWeeksLoadedMessage)
                    },
                )
        }

        private fun groupCompletedTrainingsByMonthAndWeek(
            trainings: List<CompletedTrainingShort>,
        ): List<CompletedTrainingMonth> {
            val currentWeekStart = LocalDateTime
                .now().date
                .startOfWeek()

            return trainings
                .groupBy {
                    it.startedAt.year to it.startedAt.month
                }
                .map { (yearAndMonth, completedTrainings) ->
                    CompletedTrainingMonth(
                        year = yearAndMonth.first,
                        month = yearAndMonth.second,
                        completedTrainingWeeks = completedTrainings.groupBy { training ->
                            val trainingsWeekStart = training.startedAt.date
                                .startOfWeek()
                            val weeksBetween = currentWeekStart
                                .daysUntil(trainingsWeekStart) / 7
                            -weeksBetween
                        }.map { (weekOrdinal, completedTrainings) ->
                            CompletedTrainingWeek(
                                weekOrdinal = weekOrdinal,
                                completedTrainings = completedTrainings,
                            )
                        }
                            .sortedBy { it.weekOrdinal }
                    )
                }
        }

        fun LocalDate.startOfWeek(): LocalDate {
            val dayOfWeekIso = this.dayOfWeek.isoDayNumber
            return this.minus(dayOfWeekIso - 1, DateTimeUnit.DAY)
        }
    }

    private object ReducerImpl :
        Reducer<HistoryStore.State, Msg> {
        override fun HistoryStore.State.reduce(msg: Msg): HistoryStore.State =
            when (msg) {
                is Msg.CompletedTrainingMonthsLoaded ->
                    copy(
                        completedTrainingMonths = msg.completedTrainingMonths,
                    )
            }
    }
}
