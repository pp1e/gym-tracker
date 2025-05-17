package com.example.gymtracker.ui.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.utils.toDp
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Composable
fun ElapsedTimeBar(
    startTime: Instant,
    isTopBarExpanded: Boolean,
    onResetClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    AnimatedContent(
        targetState = isTopBarExpanded,
        transitionSpec = {
            (
                    (slideInVertically { -it } + fadeIn())
                        .togetherWith(
                            slideOutVertically { -it } + fadeOut()
                        )
            )
                .using(
                    SizeTransform(clip = false)
            )
        },
        label = "ElapsedTimeBarAnimation",
    ) { expanded ->
        if (expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            start = UiConstants.topAppBarHorizontalPadding,
                            end = UiConstants.topAppBarHorizontalPadding,
                        )
                        .height(UiConstants.WeekdaySwitcherFullHeight),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ElapsedTime(
                        startTime = startTime,
                    )

                    IconButton(
                        onClick = {},
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(
                                    start = UiConstants.defaultPadding,
                                ),
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Choose Started At",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    IconButton(
                        onClick = onResetClick,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Update,
                            contentDescription = "Reset Training time",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    IconButton(
                        onClick = onDeleteClick,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete Training",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ElapsedTime(
    startTime: Instant,
) {
    var currentTime by remember { mutableStateOf(Clock.System.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Clock.System.now()
            delay(1000)
        }
    }

    val elapsedMillis = currentTime.toEpochMilliseconds() - startTime.toEpochMilliseconds()
    val totalSeconds = elapsedMillis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    Text(
        text = hours.toString().padStart(2, '0') +
                ":${minutes.toString().padStart(2, '0')}" +
                ":${seconds.toString().padStart(2, '0')}",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface,
    )
}
