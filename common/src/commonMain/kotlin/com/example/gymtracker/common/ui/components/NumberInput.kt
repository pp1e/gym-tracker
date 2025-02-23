package com.example.gymtracker.common.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymtracker.common.utils.toDp

//private const val RATIO = 0.38f

//@Composable
//private fun calculateNumberInputWidth(): DpSize {
//    val textSize = UiConstants.NumberInputFontSize.toDp() * 1.73f
//
//    val height = - (textSize * RATIO) / (2 * RATIO - 1)
//    val width = height * 2 + textSize
//
//    return DpSize(
//        height = height,
//        width = width,
//    )
//}

private val NUMBER_INPUT_FONT_SIZE = 20.sp

@Composable
fun NumberInput(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = Modifier
            .then(modifier)
            .height(
                NUMBER_INPUT_FONT_SIZE.toDp() + 32.dp
            )
            .aspectRatio(2.5f),
        value = value.toString(),
        onValueChange = { newValue ->
            newValue.toIntOrNull()?.let { onValueChange(it) }
        },
        textStyle = TextStyle(
            fontSize = NUMBER_INPUT_FONT_SIZE,
            textAlign = TextAlign.Center,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
        ),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
        ),

        leadingIcon = {
            IconButton(
                onClick = { onValueChange(value - 1) },
            ) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = "Decrement")
            }
        },

        trailingIcon = {
            IconButton(
                onClick = { onValueChange(value + 1) },
            ) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = "Increment")
            }
        }
    )
}
