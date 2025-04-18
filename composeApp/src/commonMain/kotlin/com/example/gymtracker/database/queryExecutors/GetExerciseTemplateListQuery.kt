package com.example.gymtracker.database.queryExecutors

import app.cash.sqldelight.Query
import com.example.gymtracker.domain.ExerciseTemplate
import database.exerciseTemplate.GetList

fun executeGetExerciseTemplateListQuery(
    getExerciseTemplateListQuery: Query<GetList>
) = getExerciseTemplateListQuery
    .executeAsList()
    .map {
        ExerciseTemplate(
            id = it.id,
            name = it.name,
            muscleGroup = it.name_,
        )
    }
