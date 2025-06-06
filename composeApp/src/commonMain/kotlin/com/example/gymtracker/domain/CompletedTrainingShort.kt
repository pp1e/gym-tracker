package com.example.gymtracker.domain

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDateTime

data class CompletedTrainingShort(
    val id: Long,
    val name: String,
    val color: Color?,
    val startedAt: LocalDateTime,
)
