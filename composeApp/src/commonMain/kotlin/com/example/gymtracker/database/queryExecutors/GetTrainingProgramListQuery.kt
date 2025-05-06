package com.example.gymtracker.database.queryExecutors

import app.cash.sqldelight.Query
import app.cash.sqldelight.async.coroutines.awaitAsList
import com.example.gymtracker.domain.TrainingProgramShort
import database.trainingProgram.GetList

suspend fun executeGetTrainingProgramListQuery(getTrainingProgramListQuery: Query<GetList>) =
    getTrainingProgramListQuery
        .awaitAsList()
        .map {
            TrainingProgramShort(
                id = it.id,
                name = it.name,
            )
        }
