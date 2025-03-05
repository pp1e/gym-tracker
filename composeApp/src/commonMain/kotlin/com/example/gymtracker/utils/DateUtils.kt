package com.example.gymtracker.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun LocalDate.Companion.now() =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun DayOfWeek.russianName() = when(this) {
    DayOfWeek.MONDAY -> "понедельник"
    DayOfWeek.TUESDAY -> "вторник"
    DayOfWeek.WEDNESDAY -> "среда"
    DayOfWeek.THURSDAY -> "четверг"
    DayOfWeek.FRIDAY -> "пятница"
    DayOfWeek.SATURDAY -> "суббота"
    DayOfWeek.SUNDAY -> "воскресенье"
    else -> {
        println("Can't find translation for day of week: $this")
        this.name
    }
}
