package com.example.gymtracker.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.domain.Approach
import com.example.gymtracker.ui.UiConstants
import kotlinx.coroutines.delay

private const val TEXT_FIELD_WEIGHT = 1f
private const val APPROACH_NUMBER_WEIGHT = 0.3f

private val CURRENT_EXERCISE_APPROACH_BOTTOM_PADDING = 7.dp
private val CURRENT_EXERCISE_NUMBER_INPUT_PADDING_VALUES =
    PaddingValues(
        horizontal = 12.dp,
    )

@Composable
fun CurrentExerciseApproaches(
    snackbarHostState: SnackbarHostState,
    approaches: List<Approach>,
    onApproachAdd: () -> Unit,
    requestApproachDeleting: (Long) -> Unit,
    cancelApproachDeleting: (Long) -> Unit,
    onRepetitionsChange: (Long, Int) -> Unit,
    onWeightChange: (Long, Float) -> Unit,
) {
    Column {
        Row(
            modifier =
                Modifier
                    .padding(bottom = CURRENT_EXERCISE_APPROACH_BOTTOM_PADDING)
                    .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION)
                    .align(Alignment.CenterHorizontally),
        ) {
            Spacer(
                modifier =
                    Modifier
                        .weight(APPROACH_NUMBER_WEIGHT)
                        .align(Alignment.CenterVertically),
            )

            ApproachLabel(
                weight = TEXT_FIELD_WEIGHT,
                text = "Повторения",
            )

            ApproachLabel(
                weight = TEXT_FIELD_WEIGHT,
                text = "Вес",
            )
        }

        val pendingDeletes = remember { mutableStateListOf<Long>() }

        pendingDeletes.forEach { id ->
            LaunchedEffect(id) {
                delay(UiConstants.ANIMATION_DEFAULT_DURATION_MILLIS)
                snackbarHostState.currentSnackbarData?.dismiss()
                val result =
                    snackbarHostState.showSnackbar(
                        message = "Подход удалён",
                        actionLabel = "Отменить",
                        duration = SnackbarDuration.Short,
                    )
                if (result == SnackbarResult.ActionPerformed) {
                    cancelApproachDeleting(id)
                }
                pendingDeletes.remove(id)
            }
        }

        for ((index, approach) in approaches.withIndex()) {
            key(approach.id) {
                SwipeToDeleteBox(
                    onDelete = {
                        requestApproachDeleting(approach.id)
                        pendingDeletes.add(approach.id)
                    },
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background),
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .padding(vertical = CURRENT_EXERCISE_APPROACH_BOTTOM_PADDING)
                                    .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION)
                                    .align(Alignment.Center),
                        ) {
                            ApproachLabel(
                                weight = APPROACH_NUMBER_WEIGHT,
                                text = (index + 1).toString(),
                            )

                            val numberInputModifier =
                                Modifier
                                    .align(Alignment.CenterVertically)
                                    .weight(TEXT_FIELD_WEIGHT)
                                    .padding(CURRENT_EXERCISE_NUMBER_INPUT_PADDING_VALUES)

                            NumberInput(
                                value = approach.repetitions,
                                onValueChange = { onRepetitionsChange(approach.id, it) },
                                modifier = numberInputModifier,
                            )

                            NumberInput(
                                value =
                                    approach.weight?.toInt()
                                        ?: 0,
                                // TODO Сделать по нормальному
                                onValueChange = { onWeightChange(approach.id, it.toFloat()) },
                                modifier = numberInputModifier,
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION)
                    .align(Alignment.CenterHorizontally),
        ) {
            IconButton(
                onClick = onApproachAdd,
            ) {
                Icon(
                    modifier =
                        Modifier
                            .size(UiConstants.calculateNumberInputHeight())
                            .align(Alignment.CenterStart),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить подход",
                    tint = UiConstants.getApproachFontColor(),
                )
            }
        }

        Spacer(modifier = Modifier.height(CURRENT_EXERCISE_APPROACH_BOTTOM_PADDING))
    }
}

@Composable
private fun RowScope.ApproachLabel(
    text: String,
    weight: Float,
) {
    Text(
        modifier =
            Modifier
                .weight(weight)
                .align(Alignment.CenterVertically),
        text = text,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = UiConstants.getApproachFontColor(),
    )
}
