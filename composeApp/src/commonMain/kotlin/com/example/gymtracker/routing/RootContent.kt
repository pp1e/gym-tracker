package com.example.gymtracker.routing

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.gymtracker.ui.screens.CurrentTrainingScreen
import com.example.gymtracker.ui.screens.EditTrainingScreen
import com.example.gymtracker.ui.screens.HistoryScreen
import com.example.gymtracker.ui.screens.ScheduleScreen

@Composable
fun RootContent(
    router: RootRouter,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    isTopBarExpanded: Boolean,
) {
    Children(
        stack = router.childStack,
        animation = stackAnimation(animator = fade()),
    ) {
        when (val child = it.instance) {
            is Child.CurrentTraining ->
                CurrentTrainingScreen(
                    component = child.component,
                    paddingValues = paddingValues,
                    snackbarHostState = snackbarHostState,
                    isTopBarExpanded = isTopBarExpanded,
                )

            is Child.Schedule ->
                ScheduleScreen(
                    component = child.component,
                    paddingValues = paddingValues,
                    snackbarHostState = snackbarHostState,
                )

            is Child.History ->
                HistoryScreen(
                    component = child.component,
                    paddingValues = paddingValues,
                )

            is Child.EditTraining ->
                EditTrainingScreen(
                    component = child.component,
                    paddingValues = paddingValues,
                    snackbarHostState = snackbarHostState,
                    isTopBarExpanded = isTopBarExpanded,
                )
        }
    }
}
