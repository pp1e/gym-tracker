package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.domain.TrainingProgramShort
import com.example.gymtracker.ui.UiConstants

private val TRAINING_TITLE_PADDING_VALUES =
    PaddingValues(
        vertical = 20.dp,
    )

private val FONT_SIZE = 24.sp
private val ICON_PADDING = 8.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.TrainingTitle(
    value: String?,
    onValueChange: (String) -> Unit,
    trainingProgramChoices: List<TrainingProgramShort>,
    onTrainingProgramChoose: (Long) -> Unit,
    onCreateNewClick: () -> Unit,
    createNewPlaceholder: String,
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(TRAINING_TITLE_PADDING_VALUES)
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryEditable),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = value ?: "Выберите программу",
                    onValueChange = onValueChange,
                    textStyle =
                        TextStyle(
                            fontSize = FONT_SIZE,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                        ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    singleLine = true,
                    enabled = value != null,
                    modifier = Modifier.weight(1f),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            expanded = false
                        }
                    )
                )

                Icon(
                    Icons.Rounded.Edit, contentDescription = "edit",
                    modifier = Modifier.padding(start = ICON_PADDING)
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                @Composable
                fun ComboBoxItem(
                    text: String,
                    onChosen: () -> Unit,
                    isFontBold: Boolean = false,
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = text,
                                maxLines = 1,
                                fontWeight = if (isFontBold) FontWeight.ExtraBold else null,
                                fontSize = 17.sp,
                            )
                        },
                        onClick = {
                            onChosen()
                            expanded = false
                            focusManager.clearFocus()
                        },
                    )
                }

                ComboBoxItem(
                    text = createNewPlaceholder,
                    onChosen = onCreateNewClick,
                    isFontBold = true,
                )
                if (trainingProgramChoices.isNotEmpty()) {
                    HorizontalDivider()
                }
                trainingProgramChoices.forEach { trainingProgram ->
                    ComboBoxItem(
                        text = trainingProgram.name,
                        onChosen = {
                            onTrainingProgramChoose(trainingProgram.id)
                        },
                    )
                }
            }
        }

        HorizontalDivider()
    }
}
