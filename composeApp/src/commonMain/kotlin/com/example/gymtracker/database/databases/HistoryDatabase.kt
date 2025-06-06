package com.example.gymtracker.database.databases

import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.example.gymtracker.database.utils.observe
import com.example.gymtracker.database.queryExecutors.executeGetCompletedTrainingsShortQuery
import database.CompletedTrainingQueries

class HistoryDatabase(
    private val completedTrainingQueries: Single<CompletedTrainingQueries>,
) {
    fun observeCompletedTrainings() =
        completedTrainingQueries
            .map { it.getList() }
            .observe {
                executeGetCompletedTrainingsShortQuery(it)
            }
}
