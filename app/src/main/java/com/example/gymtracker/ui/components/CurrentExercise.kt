package com.example.gymtracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.UiConstants
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private val CURRENT_EXERCISE_HEIGHT = 80.dp
private val CURRENT_EXERCISE_EXPAND_ICON_SIZE = CURRENT_EXERCISE_HEIGHT * 0.6f

@Composable
fun CurrentExercise(
    snackbarHostState: SnackbarHostState,
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .height(CURRENT_EXERCISE_HEIGHT)
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

@Composable
private fun CurrentExerciseShort(
    expanded: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .fillMaxWidth(UiConstants.CURRENT_EXERCISE_WIDTH_FRACTION)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                text = "Жим лежа",
            )

            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    fontSize = 25.sp,
                    text = "10 x 2",
                )

                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(CURRENT_EXERCISE_EXPAND_ICON_SIZE)
                        .offset(x = CURRENT_EXERCISE_EXPAND_ICON_SIZE * 0.25f),
                    imageVector = if (expanded) {
                        Icons.Rounded.ExpandLess
                    } else {
                        Icons.Rounded.ExpandMore
                    },
                    contentDescription = "Expand less/more"
                )
            }
        }

        if (!expanded) {
//            HorizontalDivider(
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//                    .fillMaxWidth(UiConstants.CURRENT_EXERCISE_WIDTH_FRACTION)
//            )
        }
    }
}
