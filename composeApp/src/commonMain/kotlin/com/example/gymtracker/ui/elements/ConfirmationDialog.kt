package com.example.gymtracker.ui.elements

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.gymtracker.settings.Settings

@Composable
fun ConfirmationDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onConfirm()
                },
            ) {
                Text(Settings.language.strings.yes)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Settings.language.strings.no)
            }
        },
    )
}
