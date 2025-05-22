package com.example.gymtracker.database.operations

import com.example.gymtracker.database.queryExecutors.executeGetApproachesQuery
import com.example.gymtracker.domain.Approach
import database.ApproachQueries

suspend fun executeSwapApproachOrdinalsOperation(
    approachFrom: Approach,
    approachTo: Approach,
    exerciseId: Long,
    approachQueries: ApproachQueries,
) = approachQueries.transaction {
    val approaches = executeGetApproachesQuery(
        approachQueries.get(
            exercise_id = exerciseId
        )
    )
    val approachesToUpdate: List<Approach>
    val newApproachFromOrdinal: Int

    if (approachFrom.ordinal < approachTo.ordinal) {
        newApproachFromOrdinal = approachTo.ordinal + 1
        approachesToUpdate = approaches.filter {
            it.ordinal > approachTo.ordinal
        }
            .sortedByDescending { it.ordinal }
    }
    else {
        newApproachFromOrdinal = approachTo.ordinal
        approachesToUpdate = approaches.filter {
            it.ordinal >= approachTo.ordinal
        }
            .sortedByDescending { it.ordinal }
    }

    for (approach in approachesToUpdate) {
        approachQueries.increaseOrdinal(approach.id)
    }
    approachQueries.updateOrdinal(
        ordinal = newApproachFromOrdinal.toLong(),
        id = approachFrom.id,
    )
}
