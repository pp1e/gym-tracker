package com.example.gymtracker.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.YearMonth
import dev.darkokoa.datetimewheelpicker.core.WheelTextPicker
import kotlinx.datetime.Month

@Composable
fun MonthYearPickerDialog(
    initialMonth: Month,
    initialYear: Int,
    minYear: Int,
    minMonth: Month,
    onDismiss: () -> Unit,
    onYearMonthSelected: (YearMonth) -> Unit,
)
{
    val years = (minYear..YearMonth.now().year).toList()
    val months = Month.entries.filter {
        it.ordinal >= minMonth.ordinal && it.ordinal <= YearMonth.now().month.ordinal
    }

    var selectedYearIndex by remember { mutableStateOf(years.indexOf(initialYear)) }
    var selectedMonthIndex by remember { mutableStateOf(months.indexOf(initialMonth)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите месяц и год") },
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                WheelTextPicker(
                    texts = months.map { it.russianName() },
                    rowCount = 3,
                    startIndex = months.indexOf(initialMonth),
                    onScrollFinished = {
                        selectedMonthIndex = it
                        it
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                WheelTextPicker(
                    texts = years.map { it.toString() },
                    rowCount = 3,
                    startIndex = years.indexOf(initialYear),
                    onScrollFinished = {
                        selectedYearIndex = it
                        it
                    }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onYearMonthSelected(
                    YearMonth(
                        year = years[selectedYearIndex],
                        month = months[selectedMonthIndex],
                    )
                )
                onDismiss()
            }) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
