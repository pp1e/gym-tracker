package com.example.gymtracker.ui.elements

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.gymtracker.i18n.I18nManager

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
                Text(I18nManager.strings.yes)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(I18nManager.strings.no)
            }
        },
    )
}
