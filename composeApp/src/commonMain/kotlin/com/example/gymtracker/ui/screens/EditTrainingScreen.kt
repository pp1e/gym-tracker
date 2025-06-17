package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.gymtracker.components.editTraining.EditTrainingComponent
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.elements.AddExerciseSheet
import com.example.gymtracker.ui.elements.AdditionalTopBar
import com.example.gymtracker.ui.elements.CompletedTrainingTitle
import com.example.gymtracker.ui.elements.ConfirmationDialog
import com.example.gymtracker.ui.elements.TimeRangeBar
import com.example.gymtracker.ui.elements.TimeRangePickerDialog
import com.example.gymtracker.ui.elements.TrainingFull
import com.example.gymtracker.utils.capitalize
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@Composable
fun EditTrainingScreen(
    component: EditTrainingComponent,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    isTopBarExpanded: Boolean,
) {
    val model by component.model.subscribeAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var showChangeTrainingDurationDialog by remember { mutableStateOf(false) }
    var showDeleteTrainingDialog by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            if (model.completedTraining != null) {
                AdditionalTopBar(
                    isTopBarExpanded = isTopBarExpanded,
                ) {
                    TimeRangeBar(
                        duration = model.completedTraining!!.duration,
                        onEditClick = { showChangeTrainingDurationDialog = true },
                        onDeleteClick = { showDeleteTrainingDialog = true },
                    )
                }

                CompletedTrainingTitle(
                    value = model.completedTraining!!.name,
                    onValueChange = component::onCompletedTrainingNameChange,
                    startedAt = model.completedTraining!!.startedAt,
                )

                TrainingFull(
                    snackbarHostState = snackbarHostState,
                    training = model.completedTraining!!.training,
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

            Spacer(
                modifier =
                    Modifier
                        .padding(UiConstants.FABPanelPadding)
                        .fillMaxWidth()
                        .height(UiConstants.FABHeight),
            )
        }

        if (model.completedTraining != null) {
            ExtendedFloatingActionButton(
                onClick = { showBottomSheet = true },
                modifier =
                    Modifier
                        .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION)
                        .padding(UiConstants.FABPanelPadding)
                        .align(Alignment.BottomCenter),
            ) {
                Icon(Icons.Rounded.Add, contentDescription = I18nManager.strings.add.capitalize())
                Text(I18nManager.strings.add.capitalize())
            }
        }
    }

    if (showDeleteTrainingDialog) {
        ConfirmationDialog(
            title = I18nManager.strings.deleteTrainingFromHistory,
            onConfirm = { component.onDeleteTrainingClick() },
            onDismiss = { showDeleteTrainingDialog = false },
        )
    }

    if (showChangeTrainingDurationDialog && model.completedTraining != null) {
        TimeRangePickerDialog(
            title = I18nManager.strings.specifyStartAndEndTrainingTime,
            initialStartTime = model.completedTraining!!.startedAt.time,
            initialEndTime =
                model.completedTraining!!.let {
                    val timeZone = TimeZone.currentSystemDefault()
                    it.startedAt
                        .toInstant(timeZone)
                        .plus(it.duration)
                        .toLocalDateTime(timeZone)
                        .time
                },
            onTimeRangeSelected = { startTime, endTime ->
                val dummyDateStart = LocalDate(2000, 1, 1)
                val dummyDateEnd =
                    if (startTime > endTime) {
                        dummyDateStart.plus(1, DateTimeUnit.DAY)
                    } else {
                        dummyDateStart
                    }
                val timeZone = TimeZone.currentSystemDefault()
                component.onTimeUpdate(
                    startedAt =
                        LocalDateTime(
                            date = model.completedTraining!!.startedAt.date,
                            time = startTime,
                        ),
                    duration =
                        endTime.atDate(dummyDateEnd).toInstant(timeZone) -
                            startTime.atDate(dummyDateStart).toInstant(timeZone),
                )
            },
            onDismiss = { showChangeTrainingDurationDialog = false },
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
