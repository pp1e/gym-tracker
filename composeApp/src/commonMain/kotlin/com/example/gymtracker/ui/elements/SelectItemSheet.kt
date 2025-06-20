package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymtracker.i18n.Translatable
import com.example.gymtracker.ui.UiConstants
import kotlin.enums.EnumEntries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <E> SelectItemSheet(
    title: String,
    onDismissRequest: () -> Unit,
    items: EnumEntries<E>,
    onSelect: (E) -> Unit,
    selectedItem: E,
) where E : Enum<E>,
        E : Translatable {
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                vertical = UiConstants.defaultPadding,
                horizontal = UiConstants.defaultPadding * 3,
            )
        )

        Spacer(modifier = Modifier.height(UiConstants.defaultPadding))

        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = ( item == selectedItem ),
                        onClick = { onSelect(item) }
                    )
                    .padding(UiConstants.defaultPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = ( item == selectedItem),
                    onClick = { onSelect(item) }
                )
                Text(
                    text = item.translate(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = UiConstants.defaultPadding)
                )
            }
        }

        Spacer(modifier = Modifier.height(UiConstants.defaultPadding * 2))
    }
}
