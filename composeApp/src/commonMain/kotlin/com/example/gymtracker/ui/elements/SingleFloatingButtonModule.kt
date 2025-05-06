package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gymtracker.ui.UiConstants

@Composable
fun BoxScope.SingleFloatingButtonModule(
    iconVector: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION)
                .padding(UiConstants.FABPanelPadding)
                .align(Alignment.BottomCenter),
    ) {
        Icon(iconVector, contentDescription = text)
        Text(text)
    }
}
