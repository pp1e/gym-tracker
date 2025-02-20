package com.example.gymtracker.routing

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.example.gymtracker.ui.screens.HistoryScreen
import com.example.gymtracker.ui.screens.TrainingScreen
import com.example.gymtracker.ui.screens.ScheduleScreen

@Composable
fun RootContent(
    router: RootRouter,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    Children(
        stack = router.childStack,
        animation = stackAnimation(animator = fade())
    ) {
        when (val child = it.instance) {
            is Child.Training -> TrainingScreen(
                component = child.component,
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
            )

            is Child.Schedule -> ScheduleScreen(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
            )

            is Child.History -> HistoryScreen(
                paddingValues = paddingValues,
            )
        }
    }
}
