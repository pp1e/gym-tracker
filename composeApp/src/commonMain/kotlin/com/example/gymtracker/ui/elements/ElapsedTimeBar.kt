package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
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
import androidx.compose.ui.Modifier
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.ui.UiConstants
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Composable
fun ElapsedTimeBar(
    startTime: Instant,
    onEditClick: () -> Unit,
    onResetClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    ElapsedTime(
        startTime = startTime,
    )

    IconButton(
        onClick = onEditClick,
    ) {
        Icon(
            modifier = Modifier
                .padding(
                    start = UiConstants.defaultPadding,
                ),
            imageVector = Icons.Rounded.Edit,
            contentDescription = I18nManager.strings.selectStartedAt,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }

    IconButton(
        onClick = onResetClick,
    ) {
        Icon(
            imageVector = Icons.Rounded.Update,
            contentDescription = I18nManager.strings.resetTrainingTime,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }

    IconButton(
        onClick = onDeleteClick,
    ) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = I18nManager.strings.deleteTraining,
            tint = MaterialTheme.colorScheme.onSurface,
        )
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
