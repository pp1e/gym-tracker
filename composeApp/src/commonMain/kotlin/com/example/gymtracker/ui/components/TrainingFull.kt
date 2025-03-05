package com.example.gymtracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.UiConstants

@Composable
fun TrainingFull(
    snackbarHostState: SnackbarHostState,
) {
    Column(
        modifier =
        Modifier
            .verticalScroll(rememberScrollState()),
    ) {
        TrainingProgramName(
            title = "Бицепс + грудь",
        )

        CurrentExercise(
            snackbarHostState = snackbarHostState,
        )
        CurrentExercise(
            snackbarHostState = snackbarHostState,
        )
        CurrentExercise(
            snackbarHostState = snackbarHostState,
        )
        CurrentExercise(
            snackbarHostState = snackbarHostState,
        )
        CurrentExercise(
            snackbarHostState = snackbarHostState,
        )

        Spacer(
            modifier =
            Modifier
                .padding(UiConstants.FABPanelPadding)
                .fillMaxWidth()
                .height(UiConstants.FABHeight),
        )
    }
}