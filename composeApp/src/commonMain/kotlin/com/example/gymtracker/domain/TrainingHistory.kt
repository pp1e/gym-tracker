package com.example.gymtracker.domain

import kotlinx.datetime.LocalDateTime

data class CompleteTraining(
    val trainingId: Long,
    val name: String,
    val datetime: LocalDateTime,
)
