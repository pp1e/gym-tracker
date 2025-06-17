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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.gymtracker.components.currentTraining.CurrentTrainingComponent
import com.example.gymtracker.i18n.I18nManager
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
import com.example.gymtracker.ui.elements.inPreposition
import com.example.gymtracker.utils.capitalize
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
        ) {
            if (model.currentTraining != null) {
                AdditionalTopBar(
                    isTopBarExpanded = isTopBarExpanded,
                ) {
                    ElapsedTimeBar(
                        startTime =
                            model.currentTraining!!.startedAt
                                .toInstant(TimeZone.currentSystemDefault()),
                        onEditClick = {
                            showChangeStartedAtDialog = true
                        },
                        onResetClick = {
                            showResetTimerDialog = true
                        },
                        onDeleteClick = {
                            showDeleteTrainingDialog = true
                        },
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
                    onApproachesSwap = component::onApproachesSwap,
                    onExercisesSwap = component::onExercisesSwap,
                )
            }
        }

        if (model.currentTraining != null) {
            DualFloatingButtonModule(
                bigButtonImageVector = Icons.Rounded.Add,
                onBigButtonClicked = { showBottomSheet = true },
                bigButtonText = I18nManager.strings.add.capitalize(),
                smallButtonIconVector = Icons.Rounded.SportsScore,
                onSmallButtonClicked = { showCompleteTrainingDialog = true },
                smallButtonText = I18nManager.strings.finish,
            )
        } else {
            SingleFloatingButtonModule(
                iconVector = Icons.Rounded.RocketLaunch,
                onClick = component::onStartTrainingClick,
                text = I18nManager.strings.startTraining,
            )
        }
    }

    if (showCompleteTrainingDialog) {
        ConfirmationDialog(
            title = I18nManager.strings.finishTraining,
            onConfirm = { component.onCompleteTrainingClick() },
            onDismiss = { showCompleteTrainingDialog = false },
        )
    }

    if (showResetTimerDialog) {
        ConfirmationDialog(
            title = I18nManager.strings.resetTrainingStartTime,
            onConfirm = { component.onStartedAtUpdate(LocalDateTime.now()) },
            onDismiss = { showResetTimerDialog = false },
        )
    }

    if (showDeleteTrainingDialog) {
        ConfirmationDialog(
            title = I18nManager.strings.deleteCurrentTraining,
            onConfirm = { component.onDeleteTrainingClick() },
            onDismiss = { showDeleteTrainingDialog = false },
        )
    }

    if (showChangeStartedAtDialog && model.currentTraining != null) {
        TimePickerDialog(
            title = I18nManager.strings.selectTrainingStartTime,
            onDismiss = { showChangeStartedAtDialog = false },
            onTimeSelect = { selectedTime ->
                component.onStartedAtUpdate(
                    startedAt =
                        LocalDateTime(
                            date = model.currentTraining!!.startedAt.date,
                            time = selectedTime,
                        ),
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
                        I18nManager.strings.trainingNotSavedMessage.replace(
                            oldValue = "(%S)",
                            newValue =
                                model.currentTraining!!.startedAt.dayOfWeek.inPreposition() +
                                    " ${formatDatetime(model.currentTraining!!.startedAt)}",
                        ),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        component.onCompleteTrainingClick()
                    },
                ) {
                    Text(I18nManager.strings.save)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        component.onDeleteTrainingClick()
                    },
                ) {
                    Text(I18nManager.strings.delete)
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
