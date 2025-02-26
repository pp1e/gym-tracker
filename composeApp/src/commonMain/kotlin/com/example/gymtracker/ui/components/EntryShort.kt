package com.example.gymtracker.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.LocalCarWash
import androidx.compose.material.icons.rounded.LocalGasStation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class EntryType {
    REFUEL,
    SERVICE,
    WASH,
    OTHER,
}

private fun iconByEntryType(entryType: EntryType) =
    when (entryType) {
        EntryType.REFUEL -> Icons.Rounded.LocalGasStation
        EntryType.SERVICE -> Icons.Rounded.Build
        EntryType.WASH -> Icons.Rounded.LocalCarWash
        EntryType.OTHER -> null
    }

@Composable
fun EntryShort(
    modifier: Modifier = Modifier,
    entryType: EntryType,
) {
//    var openDialog by remember { mutableStateOf(false) }
}
