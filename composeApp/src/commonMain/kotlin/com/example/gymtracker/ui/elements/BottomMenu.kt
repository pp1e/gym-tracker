package com.example.gymtracker.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.gymtracker.settings.Settings
import com.example.gymtracker.routing.RootRouter

@Composable
fun BottomMenu(
    onScheduleClicked: () -> Unit = {},
    onTrainingClicked: () -> Unit = {},
    onHistoryClicked: () -> Unit = {},
    onProfileClick: () -> Unit,
    activeScreen: RootRouter.ScreenConfig,
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.FitnessCenter, contentDescription = Settings.language.strings.training) },
            label = { Text(Settings.language.strings.training) },
            selected = activeScreen is RootRouter.ScreenConfig.CurrentTraining,
            onClick = onTrainingClicked,
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.EditCalendar, contentDescription = Settings.language.strings.schedule) },
            label = { Text(Settings.language.strings.schedule) },
            selected = activeScreen is RootRouter.ScreenConfig.Schedule,
            onClick = onScheduleClicked,
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.Storage, contentDescription = Settings.language.strings.history) },
            label = { Text(Settings.language.strings.history) },
            selected = activeScreen is RootRouter.ScreenConfig.History || activeScreen is RootRouter.ScreenConfig.Calendar,
            onClick = onHistoryClicked,
        )
        NavigationBarItem(
            icon = { Icon(Icons.Rounded.Person, contentDescription = Settings.language.strings.profile) },
            label = { Text(Settings.language.strings.profile) },
            selected = activeScreen is RootRouter.ScreenConfig.Profile,
            onClick = onProfileClick,
        )
    }
}
