package com.example.gymtracker.ui.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
internal fun <T> rememberPrevious(value: T): T? {
    val state = remember { mutableStateOf<T?>(null) }
    SideEffect { state.value = value }
    return state.value
}
