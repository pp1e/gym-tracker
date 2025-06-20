package com.example.gymtracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.ImportExport
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gymtracker.i18n.Language
import com.example.gymtracker.settings.Settings
import com.example.gymtracker.ui.UiConstants
import com.example.gymtracker.ui.elements.SelectItemSheet
import com.example.gymtracker.ui.theme.AppTheme

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
) {
    var showSelectLanguageSheet by remember { mutableStateOf(false) }
    var showSelectThemeSheet by remember { mutableStateOf(false) }

    Box(
        Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = UiConstants.defaultPadding,
                )
        ) {

            SettingModule(
                name = Settings.language.strings.language,
                icon = Icons.Rounded.Language,
                value = Settings.language.translate(),
                onClick = {
                    showSelectLanguageSheet = true
                },
            )

            SettingModule(
                name = Settings.language.strings.theme,
                icon = Icons.Rounded.Contrast,
                value = Settings.theme.translate(),
                onClick = {
                    showSelectThemeSheet = true
                },
            )

            SettingModule(
                name = Settings.language.strings.dataImport,
                icon = Icons.Rounded.ImportExport,
                value = Settings.language.strings.soon,
                onClick = {},
                enabled = false,
            )

            SettingModule(
                name = Settings.language.strings.charts,
                icon = Icons.AutoMirrored.Rounded.TrendingUp,
                value = Settings.language.strings.soon,
                onClick = {},
                enabled = false,
            )

            SettingModule(
                name = Settings.language.strings.statistics,
                icon = Icons.Rounded.Analytics,
                value = Settings.language.strings.soon,
                onClick = {},
                enabled = false,
            )
        }
    }

    if (showSelectLanguageSheet) {
        SelectItemSheet(
            title = Settings.language.strings.selectLanguage,
            items = Language.entries,
            onSelect = {
                Settings.setLanguage(it)
            },
            selectedItem = Settings.language,
            onDismissRequest = {
                showSelectLanguageSheet = false
            },
        )
    }

    if (showSelectThemeSheet) {
        SelectItemSheet(
            title = Settings.language.strings.selectTheme,
            items = AppTheme.entries,
            onSelect = {
                Settings.setTheme(it)
            },
            selectedItem = Settings.theme,
            onDismissRequest = {
                showSelectThemeSheet = false
            }
        )
    }
}

@Composable
private fun SettingModule(
    name: String,
    icon: ImageVector,
    value: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                enabled = enabled,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height( UiConstants.defaultPadding * 2))

        Row(
            modifier = Modifier
                .fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    UiConstants.defaultPadding * 2
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    UiConstants.defaultPadding * 2
                )
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                )
                if (enabled) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                        contentDescription = Settings.language.strings.open,
                    )
                }
            }
        }

        Spacer(Modifier.height( UiConstants.defaultPadding * 2))

        HorizontalDivider(Modifier.fillMaxWidth(UiConstants.COMMON_WIDTH_FRACTION))
    }
}
