package com.example.gymtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.gymtracker.routing.RootContent
import com.example.gymtracker.routing.RootRouter
import com.example.gymtracker.ui.components.BottomMenu
import com.example.gymtracker.ui.components.TopBar
import com.example.gymtracker.ui.theme.GymTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val router = RootRouter(
            componentContext = defaultComponentContext(),
            storeFactory = DefaultStoreFactory(),
//                            database = appRepository
        )
        setContent {
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
    }
}
