package com.example.gymtracker.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.SportsScore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.gymtracker.components.currentTraining.CurrentTrainingComponent
import com.example.gymtracker.ui.elements.AddExerciseSheet
import com.example.gymtracker.ui.elements.CurrentTrainingTitle
import com.example.gymtracker.ui.elements.DualFloatingButtonModule
import com.example.gymtracker.ui.elements.ElapsedTimeBar
import com.example.gymtracker.ui.elements.SingleFloatingButtonModule
import com.example.gymtracker.ui.elements.TrainingFull
import com.example.gymtracker.ui.elements.formatDatetime
import com.example.gymtracker.ui.elements.russianInPreposition
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentTrainingScreen(
    component: CurrentTrainingComponent,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    isTopBarExpanded: Boolean,
) {
    val model by component.model.subscribeAsState()
    var showCompleteTrainingDialog by remember { mutableStateOf(false) }
    var showResetTimerDialog by remember { mutableStateOf(false) }
    var showDeleteTrainingDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val startedAtPickerState = if (model.currentTraining != null) {
        rememberTimePickerState(
            initialHour = model.currentTraining!!.startedAt.hour,
            initialMinute = model.currentTraining!!.startedAt.minute,
            is24Hour = true,
        )
    } else null

    Box(
        modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
        ) {
            if (model.currentTraining != null) {
                ElapsedTimeBar(
                    startTime = model.currentTraining!!.startedAt
                        .toInstant(TimeZone.currentSystemDefault()),
                    isTopBarExpanded = isTopBarExpanded,
                    onResetClick = {
                        showResetTimerDialog = true
                    },
                    onDeleteClick = {
                        showDeleteTrainingDialog = true
                    }
                )

                CurrentTrainingTitle(
                    value = model.currentTraining!!.name,
                    onValueChange = component::onCurrentTrainingNameChange,
                    trainingProgramChoices = model.trainingProgramsShort,
                    onTrainingProgramChoose = component::changeTrainingProgram,
                )

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
            DualFloatingButtonModule(
                bigButtonImageVector = Icons.Rounded.Add,
                onBigButtonClicked = { showBottomSheet = true },
                bigButtonText = "Добавить",
                smallButtonIconVector = Icons.Rounded.SportsScore,
                onSmallButtonClicked = { showCompleteTrainingDialog = true },
                smallButtonText = "Завершить",
            )
        } else {
            SingleFloatingButtonModule(
                iconVector = Icons.Rounded.RocketLaunch,
                onClick = component::onStartTrainingClick,
                text = "Начать тренировку",
            )
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
                    },
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

    if (showResetTimerDialog) {
        AlertDialog(
            onDismissRequest = { showResetTimerDialog = false },
            title = { Text("Сбросить время начала тренировки?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetTimerDialog = false
                        component.onResetTrainingTimeClick()
                    },
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetTimerDialog = false }) {
                    Text("Нет")
                }
            },
        )
    }

    if (showDeleteTrainingDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteTrainingDialog = false },
            title = { Text("Удалить текущую тренировку?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteTrainingDialog = false
                        component.onDeleteTrainingClick()
                    },
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteTrainingDialog = false }) {
                    Text("Нет")
                }
            },
        )
    }

    if (model.isTrainingIrrelevant) {
        AlertDialog(
            onDismissRequest = {
                component.onDeleteTrainingClick()
            },
            title = {
                Text(
                    text =
                        "Вы не сохранили тренировку, начатую" +
                            " ${model.currentTraining!!.startedAt.dayOfWeek.russianInPreposition()}" +
                            " ${formatDatetime(model.currentTraining!!.startedAt)}." +
                            " Желаете сохранить эту тренировку?" +
                            "\n* Сохранённые тренировки можно редактировать в истории.",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        component.onCompleteTrainingClick()
                    },
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        component.onDeleteTrainingClick()
                    },
                ) {
                    Text("Удалить")
                }
            },
        )
    }

    if (showBottomSheet) {
        AddExerciseSheet(
            onDismissRequest = { showBottomSheet = false },
            exerciseTemplateNames = model.exerciseTemplateNames,
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
