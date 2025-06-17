package com.example.gymtracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymtracker.i18n.I18nManager
import com.example.gymtracker.i18n.Language
import com.example.gymtracker.ui.UiConstants
import dev.darkokoa.datetimewheelpicker.core.WheelTextPicker

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
) {
    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Язык",
                    style = MaterialTheme.typography.titleLarge,
                )
                WheelTextPicker(
                    texts = Language.entries.map { it.name },
                    rowCount = 3,
                    onScrollFinished = { index ->
                        I18nManager.setLanguage(
                            Language.entries[index]
                        )
                        index
                    },
                    startIndex = Language.entries.indexOf(I18nManager.currentLanguage),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Импорт данных",
                    style = MaterialTheme.typography.titleLarge,
                )

                Text(
                    text = "Скоро",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}
