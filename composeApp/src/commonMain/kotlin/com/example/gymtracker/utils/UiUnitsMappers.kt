package com.example.gymtracker.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified

private fun Density.spToDp(sp: TextUnit) = if (sp.isSpecified) sp.toDp() else Dp.Unspecified

@Composable
fun TextUnit.toDp() = LocalDensity.current.spToDp(this)
