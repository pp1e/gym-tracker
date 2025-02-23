package com.example.gymtracker.common.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gymtracker.common.ui.UiConstants.WeekdaySwitcherDaySize
import com.example.gymtracker.common.ui.UiConstants.WeekdaySwitcherPaddingValues

private val WEEKDAY_SWITCHER_FULL_HEIGHT = WeekdaySwitcherPaddingValues.calculateTopPadding() +
        WeekdaySwitcherPaddingValues.calculateBottomPadding() +
        WeekdaySwitcherDaySize
private val TODO_APP_BAR_HORIZONTAL_PADDING = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    screenTitle: String,
    isScheduleScreenActive: Boolean,
    isHistoryScreenActive: Boolean,
    isEditTrainingScreenActive: Boolean,
    onBackClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier
                    .padding(
                        end = TODO_APP_BAR_HORIZONTAL_PADDING,
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .height(TopAppBarDefaults.TopAppBarExpandedHeight),
                ) {
                    Text(
                        text = screenTitle,
                        modifier = Modifier.align(Alignment.CenterStart),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (isScheduleScreenActive) {
                    WeekdaySwitcher(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        },
        navigationIcon = {
            if (isEditTrainingScreenActive) {
                IconButton(
                    modifier = Modifier
                        .height(TopAppBarDefaults.TopAppBarExpandedHeight),
                    onClick = onBackClicked,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (isHistoryScreenActive) {
                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = null,
                    )
                }
            }
        },
        expandedHeight = if (isScheduleScreenActive) {
            TopAppBarDefaults.TopAppBarExpandedHeight + WEEKDAY_SWITCHER_FULL_HEIGHT
        } else TopAppBarDefaults.TopAppBarExpandedHeight,
    )
}
