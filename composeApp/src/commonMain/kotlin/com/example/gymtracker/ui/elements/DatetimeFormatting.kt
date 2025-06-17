package com.example.gymtracker.ui.elements

import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.i18n.Language
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.char

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

    return when (I18nManager.currentLanguage) {
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
        DayOfWeek.TUESDAY -> I18nManager.strings.at2
        else -> I18nManager.strings.at1
    }

fun DayOfWeek.translation() =
    when (this) {
        DayOfWeek.MONDAY -> I18nManager.strings.monday
        DayOfWeek.TUESDAY -> I18nManager.strings.tuesday
        DayOfWeek.WEDNESDAY -> I18nManager.strings.wednesday
        DayOfWeek.THURSDAY -> I18nManager.strings.thursday
        DayOfWeek.FRIDAY -> I18nManager.strings.friday
        DayOfWeek.SATURDAY -> I18nManager.strings.saturday
        DayOfWeek.SUNDAY -> I18nManager.strings.sunday
        else -> {
            println("Can't find translation for day of week: $this")
            this.name
        }
    }

fun DayOfWeek.shortTranslation() =
    when (this) {
        DayOfWeek.MONDAY -> I18nManager.strings.mondayShort
        DayOfWeek.TUESDAY -> I18nManager.strings.tuesdayShort
        DayOfWeek.WEDNESDAY -> I18nManager.strings.wednesdayShort
        DayOfWeek.THURSDAY -> I18nManager.strings.thursdayShort
        DayOfWeek.FRIDAY -> I18nManager.strings.fridayShort
        DayOfWeek.SATURDAY -> I18nManager.strings.saturdayShort
        DayOfWeek.SUNDAY -> I18nManager.strings.sundayShort
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
        Month.JANUARY -> I18nManager.strings.january
        Month.FEBRUARY -> I18nManager.strings.february
        Month.MARCH -> I18nManager.strings.march
        Month.APRIL -> I18nManager.strings.april
        Month.MAY -> I18nManager.strings.may
        Month.JUNE -> I18nManager.strings.june
        Month.JULY -> I18nManager.strings.july
        Month.AUGUST -> I18nManager.strings.august
        Month.SEPTEMBER -> I18nManager.strings.september
        Month.OCTOBER -> I18nManager.strings.october
        Month.NOVEMBER -> I18nManager.strings.november
        Month.DECEMBER -> I18nManager.strings.december
        else -> {
            println("Can't find translation for month: $this")
            this.name
        }
    }
