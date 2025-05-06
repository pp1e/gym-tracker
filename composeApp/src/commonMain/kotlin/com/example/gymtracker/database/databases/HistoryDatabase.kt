package com.example.gymtracker.database.databases

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.example.gymtracker.database.observe
import com.example.gymtracker.domain.CompletedTrainingShort
import database.CompletedTrainingQueries
import kotlinx.datetime.LocalDateTime

class HistoryDatabase(
    private val completedTrainingQueries: Single<CompletedTrainingQueries>,
) {
    fun observeCompletedTrainings() =
        completedTrainingQueries
            .map { it.getList() }
            .observe {
                it
                    .executeAsList()
                    .map { completedTraining ->
                        CompletedTrainingShort(
                            name = completedTraining.name,
                            id = completedTraining.id,
                            startedAt =
                                LocalDateTime.parse(
                                    completedTraining.started_at,
                                ),
                        )
                    }
            }
}
