package com.example.gymtracker.domain

import kotlinx.datetime.LocalDateTime

data class CurrentTraining(
    val name: String,
    val training: Training,
    val startedAt: LocalDateTime,
)
