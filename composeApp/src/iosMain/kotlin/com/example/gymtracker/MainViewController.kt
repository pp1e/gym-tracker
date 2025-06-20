package com.example.gymtracker

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.gymtracker.database.DatabasesBuilder
import com.example.gymtracker.database.DriverFactory
import com.example.gymtracker.routing.RootRouter
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    val router =
        RootRouter(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            storeFactory = DefaultStoreFactory(),
            databasesBuilder = DatabasesBuilder(
                driverFactory = DriverFactory(),
            ),
        )

    return ComposeUIViewController {
        GymTrackerApplication(
            router = router,
        )
    }
}
