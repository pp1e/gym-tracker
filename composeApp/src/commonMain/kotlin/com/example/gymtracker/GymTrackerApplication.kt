package com.example.gymtracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.gymtracker.database.DatabasesBuilder
import com.example.gymtracker.routing.RootContent
import com.example.gymtracker.routing.RootRouter
import com.example.gymtracker.ui.elements.BottomMenu
import com.example.gymtracker.ui.elements.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme

@Composable
fun GymTrackerApplication(
    databasesBuilder: DatabasesBuilder,
) {
    val router =
        RootRouter(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            storeFactory = DefaultStoreFactory(),
            databasesBuilder = databasesBuilder,
        )

    GymTrackerTheme {
        val model by router.model.subscribeAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    screenTitle = model.screenTitle,
                    isScheduleScreenActive = model.isScheduleScreenActive,
                    isHistoryScreenActive = model.isHistoryScreenActive,
                    isEditTrainingScreenActive = model.isEditTrainingScreenActive,
                    onBackClicked = router::onBackClicked,
                    onWeekdaySwitch = router::onWeekdaySwitch,
                )
            },
            bottomBar = {
                BottomMenu(
                    onTrainingClicked = router::onCurrentTrainingScreenMenuButtonClicked,
                    onScheduleClicked = router::onScheduleScreenMenuButtonClicked,
                    onHistoryClicked = router::onHistoryScreenMenuButtonClicked,
                    isTrainingScreenActive = model.isCurrentTrainingScreenActive,
                    isScheduleScreenActive = model.isScheduleScreenActive,
                    isHistoryScreenActive = model.isHistoryScreenActive,
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { paddingValues ->
            RootContent(
                router = router,
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}
