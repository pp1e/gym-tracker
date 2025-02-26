package com.example.gymtracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun CurrentExercise(snackbarHostState: SnackbarHostState) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
//                .height(CURRENT_EXERCISE_HEIGHT)
                    .clickable { expanded = !expanded },
        ) {
            SwipeToDeleteBox(
                onDelete = {},
                snackbarHostState = snackbarHostState,
            ) {
                CurrentExerciseShort(expanded)
            }
        }

        if (expanded) {
            CurrentExerciseApproaches(
                snackbarHostState = snackbarHostState,
            )

//            HorizontalDivider(
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .fillMaxWidth(UiConstants.CURRENT_EXERCISE_WIDTH_FRACTION)
//            )
        }
    }
}
