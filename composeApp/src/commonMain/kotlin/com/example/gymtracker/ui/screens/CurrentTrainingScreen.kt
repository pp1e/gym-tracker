package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.gymtracker.components.currentTraining.CurrentTrainingComponent
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.elements.AddExerciseSheet
import com.example.gymtracker.ui.elements.TrainingFull
import com.example.gymtracker.ui.elements.TrainingTitle

private val FAB_SPACE_BETWEEN = 12.dp

@Composable
fun CurrentTrainingScreen(
    component: CurrentTrainingComponent,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    val model by component.model.subscribeAsState()
    var showCompleteTrainingDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TrainingTitle(
                value = model.currentTraining?.name,
                onValueChange = component::onCurrentTrainingNameChange,
                trainingProgramChoices = model.trainingProgramsShort,
                onTrainingProgramChoose = component::changeTrainingProgram,
                onCreateNewClick = component::createNewTraining,
                createNewPlaceholder = "Создать пустую тренировку"
            )

            if (model.currentTraining != null) {
                TrainingFull(
                    snackbarHostState = snackbarHostState,
                    training = model.currentTraining!!.training,
                    onApproachAdd = component::onApproachAdd,
                    onRepetitionsChange = component::onApproachRepetitionsChange,
                    onWeightChange = component::onApproachWeightChange,
                    requestExerciseDeleting = component::requestExerciseDeleting,
                    cancelExerciseDeleting = component::cancelExerciseDeleting,
                    requestApproachDeleting = component::requestApproachDeleting,
                    cancelApproachDeleting = component::cancelApproachDeleting,
                )
            }
        }

        if (model.currentTraining != null) {
            Row(
                modifier =
                    Modifier
                        .padding(UiConstants.FABPanelPadding)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ExtendedFloatingActionButton(
                    onClick = { showBottomSheet = true },
                    modifier =
                        Modifier
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
                    modifier =
                        Modifier
                            .padding(
                                start = FAB_SPACE_BETWEEN,
                            )
                            .weight(1 - UiConstants.FAB_ADD_WEIGHT),
                ) {
                    Icon(Icons.Rounded.Done, contentDescription = "Завершить")
                }
            }
        }
    }

    if (showCompleteTrainingDialog) {
        AlertDialog(
            onDismissRequest = { showCompleteTrainingDialog = false },
            title = { Text("Завершить тренировку?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCompleteTrainingDialog = false
                        component.onCompleteTrainingClick()
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompleteTrainingDialog = false }) {
                    Text("Нет")
                }
            },
        )
    }

    if (showBottomSheet) {
        AddExerciseSheet(
            onDismissRequest = { showBottomSheet = false },
            exerciseTemplateNames = emptyList(),
            exerciseName = model.exerciseName,
            onExerciseNameChanged = component::onExerciseNameChange,
            approachesCount = model.approachesCount,
            onApproachesCountChanged = component::onApproachCountChange,
            repetitionsCount = model.repetitionsCount,
            onRepetitionsCountChanged = component::onRepetitionsCountChange,
            weight = model.weight,
            onWeightChanged = component::onWeightChange,
            onAddExerciseClicked = {
                component.onAddExerciseClick()
                showBottomSheet = false
            },
        )
    }
}
