package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gymtracker.domain.TrainingProgramShort
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.ui.UiConstants
import kotlinx.datetime.LocalDateTime

private val TRAINING_TITLE_PADDING = 20.dp
private val ICON_PADDING = 8.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.TrainingProgramTitle(
    value: String?,
    onValueChange: (String) -> Unit,
    trainingProgramChoices: List<TrainingProgramShort>,
    onTrainingProgramChoose: (Long) -> Unit,
    onCreateNewClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

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
            EditableTitle(
                modifier =
                    Modifier
                        .padding(
                            vertical = TRAINING_TITLE_PADDING,
                        )
                        .menuAnchor(MenuAnchorType.PrimaryEditable),
                value =
                    EditableTitleValue.Nullable(
                        value = value,
                        placeholder = I18nManager.strings.selectProgram,
                    ),
                onValueChange = onValueChange,
                onDone = {
                    expanded = false
                },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                ExpandedMenuItem(
                    text = I18nManager.strings.createNewProgram,
                    onChosen = {
                        onCreateNewClick()
                        expanded = false
                    },
                    isFontBold = true,
                )
                if (trainingProgramChoices.isNotEmpty()) {
                    HorizontalDivider()
                }
                trainingProgramChoices.forEach { trainingProgram ->
                    ExpandedMenuItem(
                        text = trainingProgram.name,
                        onChosen = {
                            onTrainingProgramChoose(trainingProgram.id)
                            expanded = false
                        },
                    )
                }
            }
        }

        HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.CurrentTrainingTitle(
    value: String,
    onValueChange: (String) -> Unit,
    trainingProgramChoices: List<TrainingProgramShort>,
    onTrainingProgramChoose: (Long) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

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
            EditableTitle(
                modifier =
                    Modifier
                        .padding(
                            vertical = TRAINING_TITLE_PADDING,
                        )
                        .menuAnchor(MenuAnchorType.PrimaryEditable),
                value =
                    EditableTitleValue.NotNull(
                        value = value,
                    ),
                onValueChange = onValueChange,
                onDone = {
                    expanded = false
                },
            )

            if (trainingProgramChoices.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    trainingProgramChoices.forEach { trainingProgram ->
                        ExpandedMenuItem(
                            text = trainingProgram.name,
                            onChosen = {
                                onTrainingProgramChoose(trainingProgram.id)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }

        HorizontalDivider()
    }
}

private val DATE_PADDING = 10.dp

@Composable
fun ColumnScope.CompletedTrainingTitle(
    value: String,
    startedAt: LocalDateTime,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
    ) {
        EditableTitle(
            modifier =
                Modifier
                    .padding(
                        top = TRAINING_TITLE_PADDING,
                        bottom = DATE_PADDING,
                    ),
            value = EditableTitleValue.NotNull(value),
            onValueChange = onValueChange,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(end = DATE_PADDING)
                        .align(Alignment.Top),
                text = formatDatetime(startedAt),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
            HorizontalDivider(
                modifier =
                    Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
            )
        }
    }
}

sealed class EditableTitleValue {
    data class NotNull(
        val value: String,
    ) : EditableTitleValue()

    data class Nullable(
        val value: String?,
        val placeholder: String,
    ) : EditableTitleValue()
}

@Composable
private fun EditableTitle(
    modifier: Modifier,
    value: EditableTitleValue,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier =
            Modifier
                .then(modifier)
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value =
                when (value) {
                    is EditableTitleValue.NotNull -> value.value
                    is EditableTitleValue.Nullable -> value.value ?: value.placeholder
                },
            onValueChange = onValueChange,
            textStyle =
                TextStyle(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            enabled =
                when (value) {
                    is EditableTitleValue.NotNull -> true
                    is EditableTitleValue.Nullable -> value.value != null
                },
            modifier = Modifier.weight(1f),
            keyboardActions =
                KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onDone()
                    },
                ),
        )

        Icon(
            Icons.Rounded.Edit,
            contentDescription = "edit",
            modifier = Modifier.padding(start = ICON_PADDING),
        )
    }
}

@Composable
private fun ExpandedMenuItem(
    text: String,
    onChosen: () -> Unit,
    isFontBold: Boolean = false,
) {
    val focusManager = LocalFocusManager.current

    DropdownMenuItem(
        text = {
            Text(
                text = text,
                maxLines = 1,
                fontWeight = if (isFontBold) FontWeight.ExtraBold else null,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            )
        },
        onClick = {
            onChosen()
            focusManager.clearFocus()
        },
    )
}
