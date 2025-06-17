package com.example.gymtracker.database.queryExecutors

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import database.Approach

suspend fun executeGetApproachesQuery(getApproachesQuery: Query<Approach>) =
    getApproachesQuery
        .awaitAsList()
        .map {
            com.example.gymtracker.domain.Approach(
                id = it.id,
                ordinal = it.ordinal.toInt(),
                repetitions = it.repetitions.toInt(),
                weight = it.weight.toFloat(),
            )
        }
