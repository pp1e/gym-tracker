package com.example.gymtracker.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun currentDayOfWeek() =
    Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .dayOfWeek
