package com.example.gymtracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.UiConstants

private val TRAINING_TITLE_PADDING_VALUES =
    PaddingValues(
        vertical = 20.dp,
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.TrainingProgramName(title: String) {
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Бицепс + грудь", "Спина, руки", "12345678")

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
            BasicTextField(
                modifier =
                    Modifier
                        .align(Alignment.Start)
                        .padding(TRAINING_TITLE_PADDING_VALUES)
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable),
                value = title,
                onValueChange = {},
                textStyle =
                    TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
//                trailingIcon = {
//                    if (expanded) {
//                        Icon(Icons.Default.ArrowDropUp, contentDescription = "Drop Up")
//                    } else {
//                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Drop Down")
//                    }
//                },
                singleLine = true,
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item)
                        },
                        onClick = {
                            text = item
                            expanded = false
                        },
                    )
                }
            }
        }

        HorizontalDivider()
    }
}
