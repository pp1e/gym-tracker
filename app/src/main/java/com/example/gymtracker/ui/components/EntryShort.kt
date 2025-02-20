package com.example.gymtracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.LocalCarWash
import androidx.compose.material.icons.rounded.LocalGasStation
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.gymtracker.ui.UiConstants

enum class EntryType {
    REFUEL, SERVICE, WASH, OTHER
}

private fun iconByEntryType(entryType: EntryType) = when(entryType) {
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
