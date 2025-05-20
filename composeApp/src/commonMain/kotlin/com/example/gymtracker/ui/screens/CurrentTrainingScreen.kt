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
import com.example.gymtracker.ui.elements.AdditionalTopBar
import com.example.gymtracker.ui.elements.ConfirmationDialog
import com.example.gymtracker.ui.elements.CurrentTrainingTitle
import com.example.gymtracker.ui.elements.DualFloatingButtonModule
import com.example.gymtracker.ui.elements.ElapsedTimeBar
import com.example.gymtracker.ui.elements.SingleFloatingButtonModule
import com.example.gymtracker.ui.elements.TimePickerDialog
import com.example.gymtracker.ui.elements.TrainingFull
import com.example.gymtracker.ui.elements.formatDatetime
import com.example.gymtracker.ui.elements.russianInPreposition
import com.example.gymtracker.utils.now
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

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
    var showChangeStartedAtDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

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
                AdditionalTopBar(
                    isTopBarExpanded = isTopBarExpanded
                ) {
                    ElapsedTimeBar(
                        startTime = model.currentTraining!!.startedAt
                            .toInstant(TimeZone.currentSystemDefault()),
                        onEditClick = {
                            showChangeStartedAtDialog = true
                        },
                        onResetClick = {
                            showResetTimerDialog = true
                        },
                        onDeleteClick = {
                            showDeleteTrainingDialog = true
                        }
                    )
                }

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
        ConfirmationDialog(
            title = "Завершить тренировку?",
            onConfirm = { component.onCompleteTrainingClick() },
            onDismiss = { showCompleteTrainingDialog = false }
        )
    }

    if (showResetTimerDialog) {
        ConfirmationDialog(
            title = "Сбросить время начала тренировки?",
            onConfirm = { component.onStartedAtUpdate(LocalDateTime.now()) },
            onDismiss = { showResetTimerDialog = false }
        )
    }

    if (showDeleteTrainingDialog) {
        ConfirmationDialog(
            title = "Удалить текущую тренировку?",
            onConfirm = { component.onDeleteTrainingClick() },
            onDismiss = { showDeleteTrainingDialog = false }
        )
    }

    if (showChangeStartedAtDialog && model.currentTraining != null) {
        TimePickerDialog(
            title = "Выберите время начала тренировки",
            onDismiss = { showChangeStartedAtDialog = false },
            onTimeSelect = { selectedTime ->
                component.onStartedAtUpdate(
                    startedAt = LocalDateTime(
                        date = model.currentTraining!!.startedAt.date,
                        time = selectedTime,
                    )
                )
            },
            initialTime = model.currentTraining!!.startedAt.time,
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
