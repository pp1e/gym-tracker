package com.example.gymtracker.database.databases

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.example.gymtracker.database.observe
import com.example.gymtracker.domain.CompleteTraining
import database.CompleteTrainingQueries
import kotlinx.datetime.LocalDateTime

class HistoryDatabase(
    private val completeTrainingQueries: Single<CompleteTrainingQueries>,
) {
    fun observeCompleteTrainings() = completeTrainingQueries
        .map { it.get() }
        .observe {
            it
                .executeAsList()
                .map { completeTraining ->
                    CompleteTraining(
                        name = completeTraining.name,
                        trainingId = completeTraining.training_id,
                        datetime = LocalDateTime.parse(
                            completeTraining.datetime,
                        )
                    )
                }
        }
}
