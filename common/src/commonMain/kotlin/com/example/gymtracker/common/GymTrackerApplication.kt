package com.example.gymtracker.common

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.gymtracker.common.routing.RootRouter
import com.example.gymtracker.common.ui.components.TopBar
import com.example.gymtracker.common.ui.theme.GymTrackerTheme
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.gymtracker.common.routing.RootContent
import com.example.gymtracker.common.ui.components.BottomMenu

@Composable
fun ComponentActivity.GymTrackerApplication() {
    val router = RootRouter(
        componentContext = defaultComponentContext(),
        storeFactory = DefaultStoreFactory(),
//                            database = appRepository
    )

    GymTrackerTheme {
        val model by router.model.subscribeAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold (
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    screenTitle = model.screenTitle,
                    isScheduleScreenActive = model.isScheduleScreenActive,
                    isHistoryScreenActive = model.isHistoryScreenActive,
                    isEditTrainingScreenActive = model.isEditTrainingScreenActive,
                    onBackClicked = router::onBackClicked,
                )
            },
            bottomBar = {
                BottomMenu(
                    onTrainingClicked = router::onTrainingScreenMenuButtonClicked,
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
