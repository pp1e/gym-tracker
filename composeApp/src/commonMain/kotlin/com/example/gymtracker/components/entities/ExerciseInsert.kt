package com.example.gymtracker.components.entities

data class ExerciseInsert(
    val approaches: List<ApproachInsert>,
    val ordinal: Int,
    val exerciseTemplateId: Long,
)
