package com.example.gymtracker.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gymtracker.domain.CompletedTrainingShort
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.utils.capitalize
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.plusMonths
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}

@Composable
private fun rememberFirstMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 50f,
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

@Composable
fun TrainingsCalendar(
    completedTrainings: List<CompletedTrainingShort>,
    onDaySelected: (LocalDate) -> Unit,
) {
    val startMonth =
        completedTrainings.minOfOrNull { it.startedAt }?.let {
            YearMonth(
                year = it.year,
                month = it.month,
            )
        } ?: YearMonth.now()

    val calendarState =
        rememberCalendarState(
            startMonth = startMonth,
            endMonth = YearMonth.now(),
            firstVisibleMonth = YearMonth.now(),
        )
    val visibleMonth = rememberFirstMostVisibleMonth(calendarState, viewportPercent = 90f)
    var selectedDay: CalendarDay? by remember { mutableStateOf(null) }
    var showMonthAndYearPickerDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    CalendarTitle(
        currentMonth = visibleMonth.yearMonth,
        onNextClick = {
            coroutineScope.launch {
                calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.plusMonths(1))
            }
        },
        onPreviousClick = {
            coroutineScope.launch {
                calendarState.animateScrollToMonth(calendarState.firstVisibleMonth.yearMonth.minusMonths(1))
            }
        },
        onTextClick = {
            showMonthAndYearPickerDialog = true
        },
    )

    HorizontalCalendar(
        state = calendarState,
        dayContent = { calendarDay ->
            val completedTraining =
                completedTrainings.find {
                    it.startedAt.date == calendarDay.date
                }
            Day(
                day = calendarDay,
                isSelected = calendarDay == selectedDay,
                onClick = {
                    if (completedTraining != null) {
                        selectedDay = calendarDay
                        onDaySelected(calendarDay.date)
                    }
                },
                borderColor = completedTraining?.color,
            )
        },
        monthHeader = { month ->
            DaysOfWeekTitle(
                daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek },
            )
            HorizontalDivider(
                modifier =
                    Modifier
                        .padding(top = UiConstants.defaultPadding)
                        .fillMaxWidth(),
            )
        },
    )

    HorizontalDivider(
        modifier =
            Modifier
                .padding(bottom = UiConstants.defaultPadding)
                .fillMaxWidth(),
    )

    if (showMonthAndYearPickerDialog) {
        MonthYearPickerDialog(
            initialMonth = calendarState.firstVisibleMonth.yearMonth.month,
            initialYear = calendarState.firstVisibleMonth.yearMonth.year,
            minYear = startMonth.year,
            minMonth = startMonth.month,
            onDismiss = {
                showMonthAndYearPickerDialog = false
            },
            onYearMonthSelected = { yearMonth ->
                coroutineScope.launch {
                    calendarState.animateScrollToMonth(yearMonth)
                }
            },
        )
    }
}

private const val BORDER_DIMMING_RATIO = 0.5f
private const val OUT_DAY_DIMMING_RATIO = 0.3f
private const val OUT_DAY_BORDER_DIMMING_RATIO = BORDER_DIMMING_RATIO * OUT_DAY_DIMMING_RATIO

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit,
    borderColor: Color? = null,
) {
    val borderColorDimmed =
        borderColor?.let {
            when (day.position) {
                DayPosition.MonthDate -> if (isSelected) null else borderColor.copy(alpha = BORDER_DIMMING_RATIO)
                else -> borderColor.copy(alpha = OUT_DAY_BORDER_DIMMING_RATIO)
            }
        } ?: Color.Transparent
    Box(
        modifier =
            Modifier
                .aspectRatio(1f)
                .padding(UiConstants.defaultPadding / 2)
                .clip(CircleShape)
                .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                .border(
                    width = 2.dp,
                    color = borderColorDimmed,
                    shape = CircleShape,
                )
                .clickable(
                    enabled = day.position == DayPosition.MonthDate,
                    onClick = { onClick(day) },
                    indication = null,
                    interactionSource = null,
                ),
        contentAlignment = Alignment.Center,
    ) {
        val textColor =
            when (day.position) {
                DayPosition.MonthDate -> if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                else -> MaterialTheme.colorScheme.onBackground.copy(alpha = OUT_DAY_DIMMING_RATIO)
            }
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
        )
    }
}

@Composable
private fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.shortTranslation(),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun CalendarTitle(
    currentMonth: YearMonth,
    onTextClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier.padding(
                vertical = UiConstants.defaultPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onPreviousClick,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                contentDescription = I18nManager.strings.previous,
            )
        }
        Box(
            modifier =
                Modifier
                    .weight(1f),
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(
                            horizontal = UiConstants.defaultPadding,
                        )
                        .align(Alignment.Center)
                        .clickable(onClick = onTextClick),
                text = "${
                    currentMonth.month
                        .translation()
                        .capitalize()
                }, ${currentMonth.year}",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
            )
        }
        IconButton(
            onClick = onNextClick,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = I18nManager.strings.next,
            )
        }
    }
}
