package com.example.gymtracker.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.i18n.Language
import com.example.gymtracker.ui.UiConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLanguageSheet(
    onDismissRequest: () -> Unit,
) {
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Text(
            text = I18nManager.strings.selectLanguage,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                vertical = UiConstants.defaultPadding,
                horizontal = UiConstants.defaultPadding * 3,
            )
        )

        Spacer(modifier = Modifier.height(UiConstants.defaultPadding))

        Language.entries.forEach { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (language == I18nManager.currentLanguage),
                        onClick = { I18nManager.setLanguage(language) }
                    )
                    .padding(UiConstants.defaultPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (language == I18nManager.currentLanguage),
                    onClick = { I18nManager.setLanguage(language) }
                )
                Text(
                    text = language.translation(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = UiConstants.defaultPadding)
                )
            }
        }

        Spacer(modifier = Modifier.height(UiConstants.defaultPadding * 2))
    }
}
