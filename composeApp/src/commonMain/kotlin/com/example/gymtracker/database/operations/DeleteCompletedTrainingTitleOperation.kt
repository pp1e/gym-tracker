package com.example.gymtracker.database.operations

import database.CompletedTrainingTitleQueries

suspend fun executeDeleteCompletedTrainingTitleOperation(
    completedTrainingTitleId: Long,
    completedTrainingTitleQueries: CompletedTrainingTitleQueries,
) {
    completedTrainingTitleQueries.freeUpColor(completedTrainingTitleId)
    completedTrainingTitleQueries.deleteIfPossible(completedTrainingTitleId)
}
