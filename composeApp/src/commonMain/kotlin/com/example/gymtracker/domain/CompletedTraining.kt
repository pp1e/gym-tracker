package com.example.gymtracker.domain

import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

data class CompletedTraining(
    val id: Long,
    val name: String,
    val startedAt: LocalDateTime,
    val duration: Duration,
    val training: Training,
)
