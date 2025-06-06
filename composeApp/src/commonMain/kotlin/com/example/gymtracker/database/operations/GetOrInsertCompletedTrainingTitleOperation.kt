package com.example.gymtracker.database.operations

import app.cash.sqldelight.async.coroutines.awaitAsOne
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import database.CompletedTrainingTitleQueries

suspend fun executeGetOrInsertCompletedTrainingTitleOperation(
    trainingName: String,
    completedTrainingTitleQueries: CompletedTrainingTitleQueries,
): Long = completedTrainingTitleQueries
    .getId(trainingName)
    .awaitAsOneOrNull() ?: run {
    val freeColorId = completedTrainingTitleQueries
        .getFreeColorId()
        .awaitAsOneOrNull()

    if (freeColorId == null) {
        completedTrainingTitleQueries
            .insert(trainingName)
        completedTrainingTitleQueries
            .getId(trainingName)
            .awaitAsOne()
    } else {
        completedTrainingTitleQueries
            .occupyColor(
                id = freeColorId,
                name = trainingName,
            )
        completedTrainingTitleQueries
            .getId(trainingName)
            .awaitAsOne()
    }
}
