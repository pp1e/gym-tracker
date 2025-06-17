package com.example.gymtracker.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.Companion.now() =
    Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

fun currentDayOfWeek() =
    LocalDateTime
        .now()
        .dayOfWeek
