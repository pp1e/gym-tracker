package com.example.gymtracker.common.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SUBTITLE_TEXT_SIZE = 26.sp
private val SUBTITLE_TEXT_TOP_PADDING = 10.dp

@Composable
fun ColumnScope.SubtitleText(
    text: String,
) {
    Text(
        text = text,
        modifier = Modifier
            .align(Alignment.Start)
            .padding(
                top = SUBTITLE_TEXT_TOP_PADDING
            ),
        fontSize = SUBTITLE_TEXT_SIZE,
        fontWeight = FontWeight.Medium
    )
}
