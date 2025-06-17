package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.ui.UiConstants

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
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        val focusManager = LocalFocusManager.current

        Column(
            modifier =
                Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            ComboBoxWithInput(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = ELEMENTS_VERTICAL_PADDING,
                        ),
                items = exerciseTemplateNames,
                value = exerciseName,
                onValueChange = onExerciseNameChanged,
                label = I18nManager.strings.exerciseName,
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                //                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column {
                    val modifier =
                        Modifier
                            .padding(
                                bottom = ELEMENTS_VERTICAL_PADDING,
                            )
                            .height(UiConstants.calculateNumberInputHeight())
                            .align(Alignment.Start)

                    Label(
                        text = I18nManager.strings.approaches,
                        modifier = modifier,
                    )

                    Label(
                        text = I18nManager.strings.repetitions,
                        modifier = modifier,
                    )

                    Label(
                        text = I18nManager.strings.weight,
                        modifier = modifier,
                    )
                }

                Column {
                    val modifier =
                        Modifier
                            .padding(
                                bottom = ELEMENTS_VERTICAL_PADDING,
                            )

                    NumberInput(
                        modifier = modifier,
                        value = approachesCount,
                        onValueChange = onApproachesCountChanged,
                    )

                    NumberInput(
                        modifier = modifier,
                        value = repetitionsCount,
                        onValueChange = onRepetitionsCountChanged,
                    )

                    NumberInputEditable(
                        modifier = modifier,
                        value = weight,
                        onValueChange = { onWeightChanged(it) },
                    )
                }
            }

            Button(
                modifier =
                    Modifier
                        .padding(vertical = ELEMENTS_VERTICAL_PADDING)
                        .fillMaxWidth(),
                onClick = {
                    focusManager.clearFocus()
                    onAddExerciseClicked()
                },
            ) {
                Text(
                    text = I18nManager.strings.done,
                    fontSize = UiConstants.defaultFontSize,
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
        fontSize = UiConstants.defaultFontSize,
    )
}
