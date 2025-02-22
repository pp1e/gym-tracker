package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.example.gymtracker.components.main.MainComponent
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.components.AddExerciseSheet
import com.example.gymtracker.ui.components.CurrentExercise
import com.example.gymtracker.ui.components.TopBar
import com.example.gymtracker.ui.components.TrainingTitle

private val FAB_SPACE_BETWEEN = 12.dp

@Composable
fun CurrentTrainingScreen(
    component: MainComponent,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    val model by component.model.subscribeAsState()
    var showCompleteTrainingDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
        ) {
            TrainingTitle(
                title = "Бицепс + грудь"
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
                modifier = Modifier
                    .padding(UiConstants.FABPanelPadding)
                    .fillMaxWidth()
                    .height(UiConstants.FABHeight)
            )
        }

        Row(
            modifier = Modifier
                .padding(UiConstants.FABPanelPadding)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ExtendedFloatingActionButton(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .padding(
                        end = FAB_SPACE_BETWEEN,
                    )
                    .weight(UiConstants.FAB_ADD_WEIGHT),
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Добавить")
                Text("Добавить")
            }

            ExtendedFloatingActionButton(
                onClick = { showCompleteTrainingDialog = true },
                modifier = Modifier
                    .padding(
                        start = FAB_SPACE_BETWEEN,
                    )
                    .weight(1 - UiConstants.FAB_ADD_WEIGHT),
            ) {
                Icon(Icons.Rounded.Done, contentDescription = "Завершить")
            }
        }
    }

    if (showCompleteTrainingDialog) {
        AlertDialog(
            onDismissRequest = { showCompleteTrainingDialog = false },
            title = { Text("Завершить тренировку?") },
            confirmButton = {
                TextButton(onClick = {}) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompleteTrainingDialog = false }) {
                    Text("Нет")
                }
            }
        )
    }

    if (showBottomSheet) {
        AddExerciseSheet(
            onDismissRequest = { showBottomSheet = false },
        )
    }
}
