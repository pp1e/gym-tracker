package com.example.gymtracker

import androidx.compose.ui.window.ComposeUIViewController
import com.example.gymtracker.database.DriverFactory
import com.example.gymtracker.database.createDatabase

fun MainViewController() = ComposeUIViewController {
    GymTrackerApplication(
        database = createDatabase(
            driverFactory = DriverFactory()
        )
    )
}
