package com.example.gymtracker.ui.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.domain.Exercise
import sh.calvin.reorderable.ReorderableScope

@Composable
fun ReorderableScope.CurrentExercise(
    snackbarHostState: SnackbarHostState,
    exercise: Exercise,
    onExerciseDelete: (Long) -> Unit,
    onApproachAdd: (Long) -> Unit,
    requestApproachDeleting: (Long) -> Unit,
    cancelApproachDeleting: (Long) -> Unit,
    onRepetitionsChange: (Long, Int) -> Unit,
    onWeightChange: (Long, Float) -> Unit,
    onApproachesSwap: (Approach, Approach, Long) -> Unit,
    initialVisibility: Boolean = false,
    onExpandChange: (Boolean) -> Unit,
    expanded: Boolean,
) {
    Column {
        SwipeToDeleteBox(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onExpandChange(!expanded) },
            doBeforeAnimation = {
                onExpandChange(false)
            },
            onDelete = {
                onExerciseDelete(exercise.id)
            },
            initialVisibility = initialVisibility,
        ) {
            CurrentExerciseShort(
                expanded = expanded,
                title = exercise.template.name,
                approaches = exercise.approaches.size,
                repetitions = exercise.approaches.map { it.repetitions },
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        )
        {
            CurrentExerciseApproaches(
                snackbarHostState = snackbarHostState,
                approaches = exercise.approaches,
                onApproachAdd = { onApproachAdd(exercise.id) },
                onRepetitionsChange = onRepetitionsChange,
                onWeightChange = onWeightChange,
                requestApproachDeleting = requestApproachDeleting,
                cancelApproachDeleting = cancelApproachDeleting,
                onApproachesSwap = { approachFrom, approachTo ->
                    onApproachesSwap(approachFrom, approachTo, exercise.id)
                },
            )
        }
    }
}
