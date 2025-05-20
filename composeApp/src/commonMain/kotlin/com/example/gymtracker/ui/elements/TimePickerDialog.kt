package com.example.gymtracker.ui.elements

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.utils.now
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String,
    initialTime: LocalTime,
    onTimeSelect: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    var exceedsCurrentTimeError by remember {
        mutableStateOf(false)
    }
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val selectedTime = LocalTime(
                    hour = timePickerState.hour,
                    minute = timePickerState.minute
                )
                if (selectedTime <= LocalDateTime.now().time) {
                    exceedsCurrentTimeError = false
                    onTimeSelect(selectedTime)
                    onDismiss()
                } else {
                    exceedsCurrentTimeError = true
                }
            }) {
                Text("Подтвердить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
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
                if (exceedsCurrentTimeError) {
                    DialogErrorMessage(
                        text = "Выбранное время не может превышать текущее",
                    )
                }

                TimePicker(state = timePickerState)
            }
        }
    )
}
