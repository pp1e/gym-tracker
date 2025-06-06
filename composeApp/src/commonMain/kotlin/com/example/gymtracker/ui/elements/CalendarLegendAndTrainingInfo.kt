package com.example.gymtracker.ui.elements

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gymtracker.domain.CompletedTrainingShort
import com.example.gymtracker.domain.CompletedTrainingTitle
import com.example.gymtracker.ui.UiConstants

private val BUTTON_HEIGHT = 50.dp
private val SHAPE_SIZE = 16.dp
private val LEGEND_ITEM_SIZE = 16.dp

@Composable
fun CalendarLegendAndTrainingInfo(
    completedTrainingTitles: List<CompletedTrainingTitle>,
    selectedCompletedTrainings: List<CompletedTrainingShort>,
    onTrainingClick: (Long) -> Unit,
) {
    var selectedIndex by remember { mutableStateOf(1) }
    val segments = listOf("Тренировки", "Легенда")
    val selectedColor = MaterialTheme.colorScheme.surface

    LaunchedEffect(selectedCompletedTrainings) {
        if (selectedCompletedTrainings.isNotEmpty()) {
            selectedIndex = 0
        }
    }

    Column (
        modifier = Modifier
            .padding(
                vertical = UiConstants.defaultPadding,
            )
            .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            space = 0.dp,
        ) {
            segments.forEachIndexed { index, label ->
                SegmentedButton(
                    modifier = Modifier.height(BUTTON_HEIGHT),
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    shape = RoundedCornerShape(
                        topStart = SHAPE_SIZE,
                        topEnd = SHAPE_SIZE,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    enabled = index == 1 || selectedCompletedTrainings.isNotEmpty(),
                    border = BorderStroke(width = 0.dp, color = Color.Transparent),
                    colors = SegmentedButtonDefaults.colors().copy(
                        activeContainerColor = selectedColor,
                        inactiveContainerColor = MaterialTheme.colorScheme.background,
                        disabledInactiveContainerColor = MaterialTheme.colorScheme.background,
                        disabledInactiveContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    )
                ) {
                    Text(label)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = selectedColor,
                    shape = if (selectedIndex == 0) {
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = SHAPE_SIZE,
                            bottomStart = SHAPE_SIZE,
                            bottomEnd = SHAPE_SIZE,
                        )
                    } else {
                        RoundedCornerShape(
                            topStart = SHAPE_SIZE,
                            topEnd = 0.dp,
                            bottomStart = SHAPE_SIZE,
                            bottomEnd = SHAPE_SIZE
                        )
                    }
                )
                .animateContentSize(),
        ) {
            if (selectedIndex == 1) {
                Column(modifier = Modifier.padding(LEGEND_ITEM_SIZE / 2)) {
                    completedTrainingTitles.forEach { completedTrainingTitle ->
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = LEGEND_ITEM_SIZE / 4)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(LEGEND_ITEM_SIZE)
                                    .background(
                                        color = completedTrainingTitle.color.copy(alpha = 0.75f),
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(LEGEND_ITEM_SIZE / 2))
                            Text(text = completedTrainingTitle.name)
                        }
                    }
                }
            } else {
                Column {
                    for (completedTraining in selectedCompletedTrainings) {
                        CompletedTrainingEntry(
                            completedTraining = completedTraining,
                            onClicked = onTrainingClick,
                        )
                    }
                }
            }
        }
    }
}
