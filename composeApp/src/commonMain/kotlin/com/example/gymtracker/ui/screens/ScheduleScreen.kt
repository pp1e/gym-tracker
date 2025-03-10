package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.components.AddExerciseSheet
import com.example.gymtracker.ui.components.CurrentExercise
import com.example.gymtracker.ui.components.TrainingFull

@Composable
fun ScheduleScreen(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
//    val model by component.model.subscribeAsState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
    ) {
        TrainingFull(
            snackbarHostState = snackbarHostState,
        )

        ExtendedFloatingActionButton(
            onClick = { showBottomSheet = true },
            modifier =
                Modifier
                    .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION)
                    .padding(UiConstants.FABPanelPadding)
                    .align(Alignment.BottomCenter),
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Добавить")
            Text("Добавить")
        }
    }

    if (showBottomSheet) {
        AddExerciseSheet(
            onDismissRequest = { showBottomSheet = false },
        )
    }
}
