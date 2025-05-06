package com.example.gymtracker.domain

import kotlinx.datetime.LocalDateTime

data class CompletedTrainingShort(
    val id: Long,
    val name: String,
    val startedAt: LocalDateTime,
)
