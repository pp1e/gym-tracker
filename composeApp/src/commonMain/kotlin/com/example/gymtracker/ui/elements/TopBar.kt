package com.example.gymtracker.ui.elements

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.routing.RootRouter
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.utils.capitalize
import com.example.gymtracker.utils.currentDayOfWeek
import kotlinx.datetime.DayOfWeek

private fun generateScreenTitle(activeScreen: RootRouter.ScreenConfig) =
    when (activeScreen) {
        is RootRouter.ScreenConfig.CurrentTraining ->
            "${
                currentDayOfWeek()
                    .translation()
                    .capitalize()
            }, ${I18nManager.strings.currentTraining}"
        is RootRouter.ScreenConfig.EditTraining -> I18nManager.strings.editTraining
        is RootRouter.ScreenConfig.History -> I18nManager.strings.trainingHistory
        is RootRouter.ScreenConfig.Schedule -> I18nManager.strings.editSchedule
        is RootRouter.ScreenConfig.Calendar -> I18nManager.strings.calendar
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    activeScreen: RootRouter.ScreenConfig,
    isTopAppBarExpanded: Boolean,
    onBackClicked: () -> Unit,
    selectedWeekday: DayOfWeek,
    onWeekdaySwitch: (DayOfWeek) -> Unit,
    toggleTopAppBar: () -> Unit,
    onCalenderButtonClick: () -> Unit,
    isCalendarButtonToggled: Boolean,
) {
    val isScheduleScreenActive = activeScreen is RootRouter.ScreenConfig.Schedule

    val targetHeight =
        if (isScheduleScreenActive) {
            TopAppBarDefaults.TopAppBarExpandedHeight + UiConstants.WeekdaySwitcherFullHeight
        } else {
            TopAppBarDefaults.TopAppBarExpandedHeight
        }

    val animatedHeight by animateDpAsState(
        targetValue = targetHeight,
        label = "TopAppBarHeight",
        animationSpec = tween(durationMillis = 300),
    )

    TopAppBar(
        title = {
            Column(
                modifier =
                    Modifier
                        .padding(
                            end = UiConstants.topAppBarHorizontalPadding,
                        ),
            ) {
                Box(
                    modifier =
                        Modifier
                            .height(TopAppBarDefaults.TopAppBarExpandedHeight),
                ) {
                    Text(
                        text = generateScreenTitle(activeScreen),
                        modifier = Modifier.align(Alignment.CenterStart),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(UiConstants.WeekdaySwitcherFullHeight)
                            .alpha(if (isScheduleScreenActive) 1f else 0f),
                    contentAlignment = Alignment.Center,
                ) {
                    WeekdaySwitcher(
                        modifier = Modifier.align(Alignment.Center),
                        onWeekdaySwitch = onWeekdaySwitch,
                        selectedWeekday = selectedWeekday,
                    )
                }
            }
        },
        navigationIcon = {
            if (activeScreen is RootRouter.ScreenConfig.EditTraining) {
                IconButton(
                    modifier =
                        Modifier
                            .height(TopAppBarDefaults.TopAppBarExpandedHeight),
                    onClick = onBackClicked,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = I18nManager.strings.back,
                    )
                }
            }
        },
        actions = {
            if (activeScreen is RootRouter.ScreenConfig.History || activeScreen is RootRouter.ScreenConfig.Calendar) {
                IconToggleButton(
                    checked = isCalendarButtonToggled,
                    onCheckedChange = { onCalenderButtonClick() },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = null,
                    )
                }
            } else if (activeScreen is RootRouter.ScreenConfig.CurrentTraining || activeScreen is RootRouter.ScreenConfig.EditTraining) {
                IconButton(
                    onClick = toggleTopAppBar,
                ) {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTopAppBarExpanded)
                }
            }
        },
        expandedHeight = animatedHeight,
    )
}
