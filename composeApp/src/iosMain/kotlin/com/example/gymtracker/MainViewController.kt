package com.example.gymtracker

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.gymtracker.database.DatabasesBuilder
import com.example.gymtracker.database.DriverFactory

fun MainViewController() =
    ComposeUIViewController {
        GymTrackerApplication(
            databasesBuilder =
                DatabasesBuilder(
                    driverFactory = DriverFactory(),
                ),
            componentContext = DefaultComponentContext(LifecycleRegistry()),
        )
    }
