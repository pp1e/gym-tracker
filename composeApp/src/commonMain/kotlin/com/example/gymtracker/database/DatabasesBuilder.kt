package com.example.gymtracker.database

import app.cash.sqldelight.SuspendingTransacterImpl
import com.badoo.reaktive.observable.autoConnect
import com.badoo.reaktive.observable.firstOrError
import com.badoo.reaktive.observable.replay
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.asObservable
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleOf
import com.example.gymtracker.database.databases.CalendarDatabase
import com.example.gymtracker.database.databases.CurrentTrainingDatabase
import com.example.gymtracker.database.databases.EditTrainingDatabase
import com.example.gymtracker.database.databases.HistoryDatabase
import com.example.gymtracker.database.databases.ScheduleDatabase
import kotlinx.datetime.DayOfWeek

class DatabasesBuilder(
    driverFactory: DriverFactory,
) {
    private val rootDatabase =
        singleOf(
            driverFactory
                .createDriver(),
        ).map {
            it.execute(null, "PRAGMA foreign_keys = ON;", 0)
            Database(it)
        }

    private fun <T : SuspendingTransacterImpl> Single<Database>.observeQueries(getQuery: (Database) -> T) =
        this
            .map(getQuery)
            .asObservable()
            .replay()
            .autoConnect()
            .firstOrError()
            .observeOn(ioScheduler)

    private val currentTrainingQueries =
        rootDatabase
            .observeQueries {
                it.currentTrainingQueries
            }

    private val trainingQueries =
        rootDatabase
            .observeQueries {
                it.trainingQueries
            }

    private val exerciseQueries =
        rootDatabase
            .observeQueries {
                it.exerciseQueries
            }

    private val approachQueries =
        rootDatabase
            .observeQueries {
                it.approachQueries
            }

    private val trainingScheduleQueries =
        rootDatabase
            .observeQueries {
                it.trainingScheduleQueries
            }

    private val trainingProgramQueries =
        rootDatabase
            .observeQueries {
                it.trainingProgramQueries
            }

    private val exerciseTemplateQueries =
        rootDatabase
            .observeQueries {
                it.exerciseTemplateQueries
            }

    private val completedTrainingQueries =
        rootDatabase
            .observeQueries {
                it.completedTrainingQueries
            }

    private val completedTrainingTitleQueries =
        rootDatabase
            .observeQueries {
                it.completedTrainingTitleQueries
            }

    fun createCurrentTrainingDatabase() =
        CurrentTrainingDatabase(
            currentTrainingQueries = currentTrainingQueries,
            trainingProgramQueries = trainingProgramQueries,
            trainingQueries = trainingQueries,
            exerciseQueries = exerciseQueries,
            approachQueries = approachQueries,
            exerciseTemplateQueries = exerciseTemplateQueries,
            completedTrainingQueries = completedTrainingQueries,
            trainingScheduleQueries = trainingScheduleQueries,
            completedTrainingTitleQueries = completedTrainingTitleQueries,
        )

    fun createScheduleDatabase(dayOfWeek: DayOfWeek) =
        ScheduleDatabase(
            trainingScheduleQueries = trainingScheduleQueries,
            trainingProgramQueries = trainingProgramQueries,
            trainingQueries = trainingQueries,
            exerciseQueries = exerciseQueries,
            approachQueries = approachQueries,
            exerciseTemplateQueries = exerciseTemplateQueries,
            dayOfWeek = dayOfWeek,
        )

    fun createHistoryDatabase() =
        HistoryDatabase(
            completedTrainingQueries = completedTrainingQueries,
        )

    fun createEditTrainingDatabase() =
        EditTrainingDatabase(
            completedTrainingQueries = completedTrainingQueries,
            exerciseQueries = exerciseQueries,
            approachQueries = approachQueries,
            exerciseTemplateQueries = exerciseTemplateQueries,
            completedTrainingTitleQueries = completedTrainingTitleQueries,
            database = rootDatabase,
        )

    fun createCalendarDatabase() =
        CalendarDatabase(
            completedTrainingQueries = completedTrainingQueries,
            completedTrainingTitleQueries = completedTrainingTitleQueries,
        )
}
