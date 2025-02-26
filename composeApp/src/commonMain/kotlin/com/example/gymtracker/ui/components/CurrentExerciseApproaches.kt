package com.example.gymtracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.UiConstants

private const val TEXT_FIELD_WEIGHT = 1f
private const val APPROACH_NUMBER_WEIGHT = 0.3f

private val CURRENT_EXERCISE_APPROACH_BOTTOM_PADDING = 7.dp
private val CURRENT_EXERCISE_NUMBER_INPUT_PADDING_VALUES =
    PaddingValues(
        horizontal = 12.dp,
    )

@Composable
fun ColumnScope.CurrentExerciseApproaches(snackbarHostState: SnackbarHostState) {
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

    for (i in 1..3) {
        SwipeToDeleteBox(
            snackbarHostState = snackbarHostState,
            onDelete = {},
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
                        text = "1",
                    )

                    val numberInputModifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .weight(TEXT_FIELD_WEIGHT)
                            .padding(CURRENT_EXERCISE_NUMBER_INPUT_PADDING_VALUES)

                    NumberInput(
                        value = 99,
                        onValueChange = {},
                        modifier = numberInputModifier,
                    )

                    NumberInput(
                        value = 99,
                        onValueChange = {},
                        modifier = numberInputModifier,
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(CURRENT_EXERCISE_APPROACH_BOTTOM_PADDING))
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
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
    )
}
