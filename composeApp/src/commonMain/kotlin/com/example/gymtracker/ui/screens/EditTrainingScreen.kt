package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.components.AddExerciseSheet
import com.example.gymtracker.ui.components.CurrentExercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTrainingScreen(
//    component: MainComponent,
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState()),
        ) {
            BasicTextField(
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            top = 8.dp,
//                        bottom = 8.dp,
                        )
                        .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
                value = "Грудь + ноги",
                onValueChange = {},
                textStyle =
                    TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                    ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            )

            Text(
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            bottom = 8.dp,
                        )
                        .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
                text = "Понедельник, 17 февраля",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )

            HorizontalDivider(
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
            )
//            }
            CurrentExercise(
                snackbarHostState = snackbarHostState,
            )
            CurrentExercise(
                snackbarHostState = snackbarHostState,
            )
            CurrentExercise(
                snackbarHostState = snackbarHostState,
            )
            CurrentExercise(
                snackbarHostState = snackbarHostState,
            )
            CurrentExercise(
                snackbarHostState = snackbarHostState,
            )

            Spacer(
                modifier =
                    Modifier
                        .padding(UiConstants.FABPanelPadding)
                        .fillMaxWidth()
                        .height(UiConstants.FABHeight),
            )
        }

        ExtendedFloatingActionButton(
            onClick = { showBottomSheet = true },
            modifier =
                Modifier
                    .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION)
                    .padding(UiConstants.FABPanelPadding)
                    .align(Alignment.BottomCenter),
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Добавить")
            Text("Добавить")
        }
    }

    if (showBottomSheet) {
        AddExerciseSheet(
            onDismissRequest = { showBottomSheet = false },
        )
    }
}
