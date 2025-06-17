package com.example.gymtracker.database.databases

import androidx.compose.ui.graphics.Color
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.example.gymtracker.database.queryExecutors.executeGetCompletedTrainingsShortQuery
import com.example.gymtracker.database.utils.observe
import com.example.gymtracker.domain.CompletedTrainingTitle
import database.CompletedTrainingQueries
import database.CompletedTrainingTitleQueries

class CalendarDatabase(
    private val completedTrainingQueries: Single<CompletedTrainingQueries>,
    private val completedTrainingTitleQueries: Single<CompletedTrainingTitleQueries>,
) {
    fun observeCompletedTrainings() =
        completedTrainingQueries
            .map { it.getList() }
            .observe {
                executeGetCompletedTrainingsShortQuery(it)
            }

    fun observeCompletedTrainingTitles() =
        completedTrainingTitleQueries
            .map { it.getList() }
            .observe {
                it
                    .awaitAsList()
                    .map { completedTrainingTitle ->
                        CompletedTrainingTitle(
                            name = completedTrainingTitle.name,
                            color = completedTrainingTitle.color?.let(::Color),
                        )
                    }
            }
}
