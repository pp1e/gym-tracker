package com.example.gymtracker.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.gymtracker.routing.RootRouter

@Composable
fun BottomMenu(
    onScheduleClicked: () -> Unit = {},
    onTrainingClicked: () -> Unit = {},
    onHistoryClicked: () -> Unit = {},
    activeScreen: RootRouter.ScreenConfig,
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.FitnessCenter, contentDescription = "Тренировка") },
            label = { Text("Тренировка") },
            selected = activeScreen is RootRouter.ScreenConfig.CurrentTraining,
            onClick = onTrainingClicked,
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.EditCalendar, contentDescription = "Расписание") },
            label = { Text("Расписание") },
            selected = activeScreen is RootRouter.ScreenConfig.Schedule,
            onClick = onScheduleClicked,
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.History, contentDescription = "История") },
            label = { Text("История") },
            selected = activeScreen is RootRouter.ScreenConfig.History,
            onClick = onHistoryClicked,
        )
    }
}
