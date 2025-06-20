package com.example.gymtracker.ui.elements

import com.example.gymtracker.settings.Settings
import com.example.gymtracker.i18n.Language
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

private fun dayWithSuffix(day: Int): String {
    val mod100 = day % 100
    return if (mod100 in 11..13) {
        "${day}th"
    } else {
        when (day % 10) {
            1 -> "${day}st"
            2 -> "${day}nd"
            3 -> "${day}rd"
            else -> "${day}th"
        }
    }
}

fun formatDatetime(datetime: LocalDateTime): String {
    val dayOfWeekName = datetime.dayOfWeek.translation()
    val day = datetime.dayOfMonth
    val hour = datetime.hour.toString().padStart(2, '0')
    val minute = datetime.minute.toString().padStart(2, '0')

    return when (Settings.language) {
        Language.RU -> {
            val monthName = datetime.month.russianNameGenitive()
            "$dayOfWeekName, $day $monthName, $hour:$minute"
        }
        Language.EN -> {
            val monthName = datetime.month.translation()
            "$dayOfWeekName, ${dayWithSuffix(day)} $monthName, $hour:$minute"
        }
    }
}

fun DayOfWeek.inPreposition() =
    when (this) {
        DayOfWeek.TUESDAY -> Settings.language.strings.at2
        else -> Settings.language.strings.at1
    }

fun DayOfWeek.translation() =
    when (this) {
        DayOfWeek.MONDAY -> Settings.language.strings.monday
        DayOfWeek.TUESDAY -> Settings.language.strings.tuesday
        DayOfWeek.WEDNESDAY -> Settings.language.strings.wednesday
        DayOfWeek.THURSDAY -> Settings.language.strings.thursday
        DayOfWeek.FRIDAY -> Settings.language.strings.friday
        DayOfWeek.SATURDAY -> Settings.language.strings.saturday
        DayOfWeek.SUNDAY -> Settings.language.strings.sunday
        else -> {
            println("Can't find translation for day of week: $this")
            this.name
        }
    }

fun DayOfWeek.shortTranslation() =
    when (this) {
        DayOfWeek.MONDAY -> Settings.language.strings.mondayShort
        DayOfWeek.TUESDAY -> Settings.language.strings.tuesdayShort
        DayOfWeek.WEDNESDAY -> Settings.language.strings.wednesdayShort
        DayOfWeek.THURSDAY -> Settings.language.strings.thursdayShort
        DayOfWeek.FRIDAY -> Settings.language.strings.fridayShort
        DayOfWeek.SATURDAY -> Settings.language.strings.saturdayShort
        DayOfWeek.SUNDAY -> Settings.language.strings.sundayShort
        else -> {
            println("Can't find short translation for day of week: $this")
            this.name
        }
    }

fun Month.russianNameGenitive() =
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

fun Month.translation() =
    when (this) {
        Month.JANUARY -> Settings.language.strings.january
        Month.FEBRUARY -> Settings.language.strings.february
        Month.MARCH -> Settings.language.strings.march
        Month.APRIL -> Settings.language.strings.april
        Month.MAY -> Settings.language.strings.may
        Month.JUNE -> Settings.language.strings.june
        Month.JULY -> Settings.language.strings.july
        Month.AUGUST -> Settings.language.strings.august
        Month.SEPTEMBER -> Settings.language.strings.september
        Month.OCTOBER -> Settings.language.strings.october
        Month.NOVEMBER -> Settings.language.strings.november
        Month.DECEMBER -> Settings.language.strings.december
        else -> {
            println("Can't find translation for month: $this")
            this.name
        }
    }
