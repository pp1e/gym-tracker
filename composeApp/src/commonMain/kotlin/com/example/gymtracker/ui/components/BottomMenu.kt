package com.example.gymtracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomMenu(
    onScheduleClicked: () -> Unit = {},
    onTrainingClicked: () -> Unit = {},
    onHistoryClicked: () -> Unit = {},
    isTrainingScreenActive: Boolean = false,
    isScheduleScreenActive: Boolean = false,
    isHistoryScreenActive: Boolean = false,
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.FitnessCenter, contentDescription = "Тренировка") },
            label = { Text("Тренировка") },
            selected = isTrainingScreenActive,
            onClick = onTrainingClicked,
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.EditCalendar, contentDescription = "Расписание") },
            label = { Text("Расписание") },
            selected = isScheduleScreenActive,
            onClick = onScheduleClicked,
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.AccessTime, contentDescription = "История") },
            label = { Text("История") },
            selected = isHistoryScreenActive,
            onClick = onHistoryClicked,
        )
    }
}
