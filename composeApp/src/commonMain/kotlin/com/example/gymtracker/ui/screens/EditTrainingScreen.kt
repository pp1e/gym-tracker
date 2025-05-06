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
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.elements.AddExerciseSheet
import com.example.gymtracker.ui.elements.CompletedTrainingTitle
import com.example.gymtracker.ui.elements.TrainingFull

@Composable
fun EditTrainingScreen(
    component: EditTrainingComponent,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    val model by component.model.subscribeAsState()

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
                    .fillMaxWidth(),
        ) {
            if (model.completedTraining != null) {
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
                Icon(Icons.Rounded.Add, contentDescription = "Добавить")
                Text("Добавить")
            }
        }
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
