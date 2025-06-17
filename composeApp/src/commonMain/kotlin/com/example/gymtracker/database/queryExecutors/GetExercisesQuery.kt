package com.example.gymtracker.database.queryExecutors

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import database.Exercise

suspend fun executeGetExercisesQuery(getExercisesQuery: Query<Exercise>) =
    getExercisesQuery
        .awaitAsList()
        .map {
            com.example.gymtracker.domain.ExerciseShort(
                id = it.id,
                ordinal = it.ordinal.toInt(),
            )
        }
