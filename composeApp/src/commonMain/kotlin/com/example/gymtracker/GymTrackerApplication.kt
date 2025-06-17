package com.example.gymtracker

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
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
    componentContext: ComponentContext,
) {
    val router =
        RootRouter(
            componentContext = componentContext,
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
                    activeScreen = model.activeScreenConfig,
                    onBackClicked = router::onBackClicked,
                    onWeekdaySwitch = router::onWeekdaySwitch,
                    selectedWeekday = model.selectedWeekday,
                    isTopAppBarExpanded = model.isTopBarExpanded,
                    toggleTopAppBar = router::toggleTopBar,
                    onCalenderButtonClick = router::onCalenderButtonClick,
                    isCalendarButtonToggled = model.isCalendarButtonToggled,
                )
            },
            bottomBar = {
                BottomMenu(
                    onTrainingClicked = router::onCurrentTrainingScreenMenuButtonClick,
                    onScheduleClicked = router::onScheduleScreenMenuButtonClick,
                    onHistoryClicked = router::onHistoryScreenMenuButtonClick,
                    activeScreen = model.activeScreenConfig,
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
//            contentWindowInsets = WindowInsets.displayCutout,
            contentWindowInsets = WindowInsets.systemBars,
        ) { paddingValues ->
            RootContent(
                router = router,
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                isTopBarExpanded = model.isTopBarExpanded,
            )
        }
    }
}
