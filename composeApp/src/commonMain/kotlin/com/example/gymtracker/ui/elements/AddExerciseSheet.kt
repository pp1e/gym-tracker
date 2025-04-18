package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LABEL_FONT_SIZE = 20.sp
private val ELEMENTS_VERTICAL_PADDING = 10.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseSheet(
    onDismissRequest: () -> Unit,
    exerciseTemplateNames: List<String>,
    exerciseName: String,
    onExerciseNameChanged: (String) -> Unit,
    approachesCount: Int,
    onApproachesCountChanged: (Int) -> Unit,
    repetitionsCount: Int,
    onRepetitionsCountChanged: (Int) -> Unit,
    weight: Float,
    onWeightChanged: (Float) -> Unit,
    onAddExerciseClicked: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            ComboBoxWithInput(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = ELEMENTS_VERTICAL_PADDING,
                        ),
                items = exerciseTemplateNames,
                value = exerciseName,
                onValueChange = onExerciseNameChanged,
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Label(
                        text = "Подходы",
                        modifier =
                            Modifier
                                .align(Alignment.CenterHorizontally),
                    )

                    NumberInput(
                        value = approachesCount,
                        onValueChange = onApproachesCountChanged,
                    )
                }
                Column {
                    Label(
                        text = "Повторения",
                        modifier =
                            Modifier
                                .align(Alignment.CenterHorizontally),
                    )

                    NumberInput(
                        value = repetitionsCount,
                        onValueChange = onRepetitionsCountChanged,
                    )
                }
            }

            Label(
                text = "Вес",
            )

            NumberInput(
                value = weight.toInt(),
                onValueChange = { onWeightChanged(it.toFloat()) },
            )

            Button(
                modifier =
                    Modifier
                        .padding(top = ELEMENTS_VERTICAL_PADDING * 2)
                        .fillMaxWidth(0.6f),
                onClick = onAddExerciseClicked,
            ) {
                Text(
                    text = "Готово",
                    fontSize = LABEL_FONT_SIZE,
                )
            }
        }
    }
}

@Composable
private fun Label(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier =
            Modifier
                .then(modifier)
                .padding(
                    vertical = ELEMENTS_VERTICAL_PADDING,
                ),
        fontSize = LABEL_FONT_SIZE,
    )
}
