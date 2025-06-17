package com.example.gymtracker.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.gymtracker.i18n.I18nManager
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
            icon = { Icon(Icons.Rounded.FitnessCenter, contentDescription = I18nManager.strings.training) },
            label = { Text(I18nManager.strings.training) },
            selected = activeScreen is RootRouter.ScreenConfig.CurrentTraining,
            onClick = onTrainingClicked,
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.EditCalendar, contentDescription = I18nManager.strings.schedule) },
            label = { Text(I18nManager.strings.schedule) },
            selected = activeScreen is RootRouter.ScreenConfig.Schedule,
            onClick = onScheduleClicked,
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.Storage, contentDescription = I18nManager.strings.history) },
            label = { Text(I18nManager.strings.history) },
            selected = activeScreen is RootRouter.ScreenConfig.History || activeScreen is RootRouter.ScreenConfig.Calendar,
            onClick = onHistoryClicked,
        )
    }
}
