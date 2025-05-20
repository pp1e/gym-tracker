package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.UiConstants
import kotlinx.datetime.LocalTime
import kotlin.time.Duration

@Composable
fun TimeRangeBar(
    duration: Duration,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Text(
        text = formatDuration(duration),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface,
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
            contentDescription = "Change Time",
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


private fun formatDuration(duration: Duration): String {
    val hours = duration.inWholeHours
    val minutes = duration.inWholeMinutes % 60

    val parts = mutableListOf<String>()

    if (hours > 0) {
        parts += "$hours ч"
    }

    if (minutes > 0 || hours == 0L) {
        parts += "$minutes мин"
    }

    return parts.joinToString(" ")
}
