package com.example.gymtracker.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.UiConstants

private val CURRENT_EXERCISE_SHORT_HEIGHT = 70.dp
private val CURRENT_EXERCISE_EXPAND_ICON_SIZE = CURRENT_EXERCISE_SHORT_HEIGHT * 0.6f
private val EXERCISE_NAME_FONT_SIZE = 17.sp
private val APPROACHES_AND_REPETITIONS_FONT_SIZE = 19.sp

@Composable
internal fun CurrentExerciseShort(
    expanded: Boolean,
    title: String,
    approaches: Int,
    repetitions: List<Int>,
) {
    Box(
        modifier =
            Modifier
                .height(CURRENT_EXERCISE_SHORT_HEIGHT)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
        ) {
            Text(
                modifier =
                    Modifier
                        .align(Alignment.CenterStart),
                fontSize = EXERCISE_NAME_FONT_SIZE,
//                fontWeight = FontWeight.Medium,
                text = title,
            )

            Row(
                modifier =
                    Modifier
                        .align(Alignment.CenterEnd),
            ) {
                Text(
                    modifier =
                        Modifier
                            .align(Alignment.CenterVertically),
                    fontWeight = FontWeight.Medium,
                    fontSize = APPROACHES_AND_REPETITIONS_FONT_SIZE,
                    text = "${generateRepetitionsMessage(repetitions)} x $approaches",
                )

                Icon(
                    modifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .size(CURRENT_EXERCISE_EXPAND_ICON_SIZE)
                            .offset(x = CURRENT_EXERCISE_EXPAND_ICON_SIZE * 0.25f),
                    imageVector =
                        if (expanded) {
                            Icons.Rounded.ExpandLess
                        } else {
                            Icons.Rounded.ExpandMore
                        },
                    contentDescription = "Expand less/more",
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

private fun generateRepetitionsMessage(repetitions: List<Int>): String {
    val maxRepetitions = repetitions.maxOrNull() ?: 0
    val minRepetitions = repetitions.minOrNull() ?: 0

    return if (minRepetitions == maxRepetitions) {
        "$maxRepetitions"
    } else {
        "$minRepetitions-$maxRepetitions"
    }
}
