package com.example.gymtracker.database.queryExecutors

import androidx.compose.ui.graphics.Color
import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.example.gymtracker.domain.CompletedTrainingShort
import database.completedTraining.GetList
import kotlinx.datetime.LocalDateTime

suspend fun executeGetCompletedTrainingsShortQuery(getCompletedTrainingsShortQuery: Query<GetList>) =
    getCompletedTrainingsShortQuery
        .awaitAsList()
        .mapNotNull { completedTraining ->
            completedTraining.name?.let { name ->
                CompletedTrainingShort(
                    id = completedTraining.id,
                    name = name,
                    color = completedTraining.color?.let(::Color),
                    startedAt =
                        LocalDateTime.parse(
                            completedTraining.started_at,
                        ),
                )
            }
        }
