package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.UiConstants

@Composable
fun DialogErrorMessage(
    text: String,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(bottom = UiConstants.defaultPadding),
    )
}
