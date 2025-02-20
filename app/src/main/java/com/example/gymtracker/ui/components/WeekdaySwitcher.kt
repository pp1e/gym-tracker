package com.example.gymtracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.UiConstants

enum class Day(val shortName: String) {
    MONDAY("Пн"),
    TUESDAY("Вт"),
    WEDNESDAY("Ср"),
    THURSDAY("Чт"),
    FRIDAY("Пт"),
    SATURDAY("Сб"),
    SUNDAY("Вс")
}

@Composable
fun WeekdaySwitcher(
    modifier: Modifier = Modifier,
) {
    var selectedWeekday by remember { mutableStateOf(Day.MONDAY) }

    val days = Day.entries

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(
                UiConstants.WeekdaySwitcherPaddingValues,
            ),
    ) {
        days.forEach { day ->
            Box(
                modifier = Modifier
                    .size(UiConstants.WeekdaySwitcherDaySize)
                    .clickable { selectedWeekday = day }
                    .background(
                        color = if (day == selectedWeekday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = day.shortName,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = if (day == selectedWeekday) FontWeight.Bold else FontWeight.Normal,
                        color = if (day == selectedWeekday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}
