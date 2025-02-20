package com.example.gymtracker.ui.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun TrainingTitle(

) {
    BasicTextField(
        value = "Безымянная тренировка",
        onValueChange = {},
        textStyle = TextStyle(
            fontSize = 22.sp,
        ),
    )
}
