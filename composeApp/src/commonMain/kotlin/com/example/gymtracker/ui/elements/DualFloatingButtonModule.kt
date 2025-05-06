package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.gymtracker.ui.UiConstants

private val FAB_SPACE_BETWEEN = 12.dp

@Composable
fun BoxScope.DualFloatingButtonModule(
    onSmallButtonClicked: () -> Unit,
    onBigButtonClicked: () -> Unit,
    smallButtonIconVector: ImageVector,
    bigButtonImageVector: ImageVector,
    smallButtonText: String,
    bigButtonText: String,
) {
    Row(
        modifier =
            Modifier
                .padding(UiConstants.FABPanelPadding)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ExtendedFloatingActionButton(
            onClick = onBigButtonClicked,
            modifier =
                Modifier
                    .padding(
                        end = FAB_SPACE_BETWEEN,
                    )
                    .weight(UiConstants.FAB_ADD_WEIGHT),
        ) {
            Icon(bigButtonImageVector, contentDescription = bigButtonText)
            Text(bigButtonText)
        }

        ExtendedFloatingActionButton(
            onClick = onSmallButtonClicked,
            modifier =
                Modifier
                    .padding(
                        start = FAB_SPACE_BETWEEN,
                    )
                    .weight(1 - UiConstants.FAB_ADD_WEIGHT),
        ) {
            Icon(smallButtonIconVector, contentDescription = smallButtonText)
        }
    }
}
