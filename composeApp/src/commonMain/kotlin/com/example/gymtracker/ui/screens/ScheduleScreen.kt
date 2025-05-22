package com.example.gymtracker.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.example.gymtracker.components.schedule.ScheduleComponent
import com.example.gymtracker.ui.elements.AddExerciseSheet
import com.example.gymtracker.ui.elements.SingleFloatingButtonModule
import com.example.gymtracker.ui.elements.TrainingFull
import com.example.gymtracker.ui.elements.TrainingProgramTitle

@Composable
fun ScheduleScreen(
    component: ScheduleComponent,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    val model by component.model.subscribeAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { focusManager.clearFocus() }
                },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TrainingProgramTitle(
                value = model.trainingProgram?.name,
                onValueChange = component::onTrainingProgramNameChange,
                trainingProgramChoices = model.trainingProgramsShort,
                onTrainingProgramChoose = component::changeTrainingProgram,
                onCreateNewClick = component::createNewTrainingProgram,
            )

            if (model.trainingProgram != null) {
                TrainingFull(
                    snackbarHostState = snackbarHostState,
                    training = model.trainingProgram!!.training,
                    requestExerciseDeleting = component::requestExerciseDeleting,
                    cancelExerciseDeleting = component::cancelExerciseDeleting,
                    onApproachAdd = component::onApproachAdd,
                    requestApproachDeleting = component::requestApproachDeleting,
                    cancelApproachDeleting = component::cancelApproachDeleting,
                    onWeightChange = component::onApproachWeightChange,
                    onRepetitionsChange = component::onApproachRepetitionsChange,
                    onApproachesSwap = component::onApproachesSwap,
                    onExercisesSwap = component::onExercisesSwap,
                )
            }
        }

        if (model.trainingProgram != null) {
            SingleFloatingButtonModule(
                iconVector = Icons.Rounded.Add,
                text = "Добавить",
                onClick = { showBottomSheet = true },
            )
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
