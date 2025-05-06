package com.example.gymtracker.ui.elements

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

fun formatDatetime(datetime: LocalDateTime): String {
    val dayOfWeekName = datetime.dayOfWeek.russianName()
    val day = datetime.dayOfMonth
    val monthName = datetime.month.russianName()
    val hour = datetime.hour.toString().padStart(2, '0')
    val minute = datetime.minute.toString().padStart(2, '0')

    return "$dayOfWeekName, $day $monthName, $hour:$minute"
}

fun DayOfWeek.russianInPreposition() =
    when (this) {
        DayOfWeek.TUESDAY -> "во"
        else -> "в"
    }

fun DayOfWeek.russianName() =
    when (this) {
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

fun Month.russianName() =
    when (this) {
        Month.JANUARY -> "января"
        Month.FEBRUARY -> "февраля"
        Month.MARCH -> "марта"
        Month.APRIL -> "апреля"
        Month.MAY -> "мая"
        Month.JUNE -> "июня"
        Month.JULY -> "июля"
        Month.AUGUST -> "августа"
        Month.SEPTEMBER -> "сентября"
        Month.OCTOBER -> "октября"
        Month.NOVEMBER -> "ноября"
        Month.DECEMBER -> "декабря"
        else -> {
            println("Can't find translation for month: $this")
            this.name
        }
    }
