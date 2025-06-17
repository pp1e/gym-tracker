package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.unit.dp
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.utils.now
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeRangePickerDialog(
    title: String,
    initialStartTime: LocalTime,
    initialEndTime: LocalTime,
    onTimeRangeSelected: (LocalTime, LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val startTimePickerState = rememberTimePickerState(
        initialHour = initialStartTime.hour,
        initialMinute = initialStartTime.minute
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = initialEndTime.hour,
        initialMinute = initialEndTime.minute
    )
    var endTimeNextDay by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val startTime = LocalTime(
                    hour = startTimePickerState.hour,
                    minute = startTimePickerState.minute
                )
                val endTime = LocalTime(
                    hour = endTimePickerState.hour,
                    minute = endTimePickerState.minute
                )

                when {
                    (startTime > endTime) && (!endTimeNextDay) -> {
                        errorText = I18nManager.strings.startTimeCannotExceedEndTime
                        showError = true
                    }
                    else -> {
                        showError = false
                        onTimeRangeSelected(startTime, endTime)
                        onDismiss()
                    }
                }
            }) {
                Text(I18nManager.strings.confirm)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(I18nManager.strings.cancel)
            }
        },
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (showError) {
                    DialogErrorMessage(
                        text = errorText,
                    )
                }

                DialogLabel(I18nManager.strings.begin)
                TimeInput(state = startTimePickerState)

                Spacer(modifier = Modifier.height(16.dp))

                DialogLabel(I18nManager.strings.end)
                TimeInput(state = endTimePickerState)

                Row(modifier = Modifier.padding(horizontal = UiConstants.defaultPadding)) {
                    Checkbox(
                        checked = endTimeNextDay,
                        onCheckedChange = { endTimeNextDay = it }
                    )
                    Text(
                        text = I18nManager.strings.trainingStartedAndEndedOnDifferentDays,
                    )
                }
            }
        }
    )
}

@Composable
fun DialogLabel(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = UiConstants.defaultPadding),
    )
}
