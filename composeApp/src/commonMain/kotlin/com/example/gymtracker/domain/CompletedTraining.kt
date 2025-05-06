package com.example.gymtracker.domain

import kotlinx.datetime.LocalDateTime

data class CompletedTraining(
    val name: String,
    val startedAt: LocalDateTime,
    val training: Training,
)
