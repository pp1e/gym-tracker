package com.example.gymtracker.ui.elements

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gymtracker.ui.UiConstants
import kotlinx.coroutines.delay

@Composable
fun NumberInput(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxValue: Int = 99,
    minValue: Int = 0,
) {
//    LaunchedEffect(isHolding) {
//        var increment = 1
//        while (isHolding) {
//            val newValue = value + increment
//            if (newValue <= maxValue) {
//                onValueChange(newValue)
//                increment++
//            }
//            delay(250)
//        }
//    }

    Card(
        modifier =
            Modifier
                .then(modifier)
                .height(
                    UiConstants.calculateNumberInputHeight(),
                )
                .aspectRatio(2.5f),
//        value = value.toString(),
//        onValueChange = {},
//        readOnly = true,
//        textStyle =
//            TextStyle(
//                fontSize = UiConstants.numberInputFontSize,
//                textAlign = TextAlign.Center,
//            ),
//        keyboardOptions =
//            KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Number,
//            ),
//        colors =
//            TextFieldDefaults.colors(
//                unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
//            ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    if (value > minValue) {
                        onValueChange(value - 1)
                    }
                },
            ) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = "Decrement")
            }

            Text(
                text = value.toString(),
                fontSize = UiConstants.numberInputFontSize,
                textAlign = TextAlign.Center,
            )

//            IconButton(
//                onClick = {
//                    if (value < maxValue) {
//                        onValueChange(value + 1)
//                    }
//                },
//                modifier = Modifier.pointerInput(Unit) {
//                    println("123")
//                    detectTapGestures(
//                        onPress = {
//                            isHolding = true
//                            println(isHolding)
//                            tryAwaitRelease()
//                            isHolding = false
//                            println(isHolding)
//                        }
//                    )
//                },
//            ) {
//                Icon(imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = "Increment")
//            }

            NumberInputButton(
                value = value,
                onValueChange = onValueChange,
                step = 1,
            )
        }
    }
}

@Composable
private fun NumberInputButton(
    value: Int,
    onValueChange: (Int) -> Unit,
    step: Int,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressing by remember { mutableStateOf(false) }

    LaunchedEffect(isPressing) {
        if (isPressing) {
            interactionSource.emit(PressInteraction.Press(Offset.Zero))
            var changeMagnitude = step
            while (isPressing) {
                onValueChange(value + changeMagnitude)
                delay(200)
                changeMagnitude += step
            }
        } else {
            interactionSource.emit(PressInteraction.Release(PressInteraction.Press(Offset.Zero)))
        }
    }

    Box(
        modifier =
            Modifier
                .clip(CircleShape)
                .indication(
                    interactionSource = interactionSource,
                    indication = ripple(bounded = false),
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressing = true
                            tryAwaitRelease()
                            isPressing = false
                        },
                    )
                }
                .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = "Increment")
    }
}
