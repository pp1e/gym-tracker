package com.example.gymtracker.domain

data class Exercise(
    val id: Long,
    val ordinal: Int,
    val approaches: List<Approach>,
    val template: ExerciseTemplate,
)