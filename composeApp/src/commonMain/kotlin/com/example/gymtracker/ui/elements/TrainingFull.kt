package com.example.gymtracker.ui.elements

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.Exercise
import com.example.gymtracker.domain.Training
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.utils.safeIn
import kotlinx.coroutines.delay
import sh.calvin.reorderable.ReorderableColumn
import androidx.compose.runtime.mutableStateMapOf
import com.example.gymtracker.i18n.I18nManager

@Composable
fun TrainingFull(
    snackbarHostState: SnackbarHostState,
    training: Training,
    requestExerciseDeleting: (Long) -> Unit,
    cancelExerciseDeleting: (Long) -> Unit,
    onApproachAdd: (Long) -> Unit,
    requestApproachDeleting: (Long) -> Unit,
    cancelApproachDeleting: (Long) -> Unit,
    onRepetitionsChange: (Long, Int) -> Unit,
    onWeightChange: (Long, Float) -> Unit,
    onApproachesSwap: (Approach, Approach, Long) -> Unit,
    onExercisesSwap: (Exercise, Exercise) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .animateContentSize()
                .verticalScroll(rememberScrollState()),
    ) {
        val pendingDeletes = remember { mutableStateListOf<Long>() }

        pendingDeletes.forEach { id ->
            LaunchedEffect(id) {
                delay(UiConstants.ANIMATION_DEFAULT_DURATION_MILLIS)
                snackbarHostState.currentSnackbarData?.dismiss()
                val result =
                    snackbarHostState.showSnackbar(
                        message = I18nManager.strings.exerciseDeleted.uppercase(),
                        actionLabel = I18nManager.strings.cancel.uppercase(),
                        duration = SnackbarDuration.Short,
                    )
                if (result == SnackbarResult.ActionPerformed) {
                    cancelExerciseDeleting(id)
                }
                pendingDeletes.remove(id)
            }
        }

        val previousExerciseIds = rememberPrevious(
            training.exercises.map { it.id }.toSet()
        )
        val expandedStates = remember {
            mutableStateMapOf<Long, Boolean>()
        }

        ReorderableColumn(
            list = training.exercises,
            onSettle = { fromIndex, toIndex ->
                onExercisesSwap(
                    training.exercises[fromIndex],
                    training.exercises[toIndex],
                )
            },
        ) { _, exercise, isDragging ->
            key(exercise.id) {
                val expanded = expandedStates[exercise.id] ?: false

                LaunchedEffect(isDragging) {
                    if (isDragging) {
                        for (id in expandedStates.keys)
                            expandedStates[id] = false
                    }
                }

                CurrentExercise(
                    snackbarHostState = snackbarHostState,
                    exercise = exercise,
                    onExerciseDelete = { id ->
                        requestExerciseDeleting(id)
                        pendingDeletes.add(id)
                    },
                    onApproachAdd = onApproachAdd,
                    requestApproachDeleting = requestApproachDeleting,
                    cancelApproachDeleting = cancelApproachDeleting,
                    onRepetitionsChange = onRepetitionsChange,
                    onWeightChange = onWeightChange,
                    onApproachesSwap = onApproachesSwap,
                    initialVisibility = exercise.id safeIn previousExerciseIds,
                    expanded = expanded,
                    onExpandChange = { expandedStates[exercise.id] = it },
                )
            }
        }

        Spacer(
            modifier =
                Modifier
                    .padding(UiConstants.FABPanelPadding)
                    .fillMaxWidth()
                    .height(UiConstants.FABHeight),
        )
    }
}
