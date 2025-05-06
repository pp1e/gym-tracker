package com.example.gymtracker.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.UiConstants
import kotlinx.datetime.DayOfWeek

private fun DayOfWeek.toRusShortName() =
    when (this) {
        DayOfWeek.MONDAY -> "Пн"
        DayOfWeek.TUESDAY -> "Вт"
        DayOfWeek.WEDNESDAY -> "Ср"
        DayOfWeek.THURSDAY -> "Чт"
        DayOfWeek.FRIDAY -> "Пт"
        DayOfWeek.SATURDAY -> "Сб"
        DayOfWeek.SUNDAY -> "Вс"
        else -> ""
    }

@Composable
fun WeekdaySwitcher(
    modifier: Modifier = Modifier,
    onWeekdaySwitch: (DayOfWeek) -> Unit,
    selectedWeekday: DayOfWeek,
) {
    val days = DayOfWeek.entries

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier
                .then(modifier)
                .fillMaxWidth()
                .padding(
                    UiConstants.WeekdaySwitcherPaddingValues,
                ),
    ) {
        days.forEach { day ->
            Box(
                modifier =
                    Modifier
                        .size(UiConstants.WeekdaySwitcherDaySize)
                        .clickable {
                            onWeekdaySwitch(day)
                        }
                        .background(
                            color = if (day == selectedWeekday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            shape = CircleShape,
                        ),
            ) {
                Text(
                    text = day.toRusShortName(),
                    style =
                        TextStyle(
                            fontSize = 15.sp,
                            fontWeight = if (day == selectedWeekday) FontWeight.Bold else FontWeight.Normal,
                            color = if (day == selectedWeekday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        ),
                    modifier =
                        Modifier
                            .align(Alignment.Center),
                )
            }
        }
    }
}
