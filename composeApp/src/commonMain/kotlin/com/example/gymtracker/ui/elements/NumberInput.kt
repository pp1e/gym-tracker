package com.example.gymtracker.ui.elements

import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
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
    Card(
        modifier =
            Modifier
                .then(modifier)
                .height(
                    UiConstants.calculateNumberInputHeight(),
                )
                .aspectRatio(2.5f),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NumberInputButton(
                value = value.toFloat(),
                onValueChange = {
                    if (it >= minValue) {
                        onValueChange(it.toInt())
                    }
                },
                step = -1,
                iconVector = Icons.Rounded.KeyboardArrowDown,
                iconDescription = "Decrement",
            )

            Text(
                text = value.toString(),
                fontSize = UiConstants.defaultFontSize,
                textAlign = TextAlign.Center,
            )

            NumberInputButton(
                value = value.toFloat(),
                onValueChange = {
                    if (it <= maxValue) {
                        onValueChange(it.toInt())
                    }
                },
                step = 1,
                iconVector = Icons.Rounded.KeyboardArrowUp,
                iconDescription = "Increment",
            )
        }
    }
}

@Composable
fun NumberInputEditable(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    maxValue: Int = 1000,
    minValue: Int = 0,
) {
    val focusManager = LocalFocusManager.current
    var textFieldValue by remember { mutableStateOf(value.toString()) }
    var isInput by remember { mutableStateOf(false) }

    fun emitValue() {
        val newValue = textFieldValue.toFloatOrNull()
        if (newValue != null) {
            textFieldValue = newValue.toString()
            onValueChange(newValue)
        }
        else {
            textFieldValue = value.toString()
        }
    }

    Card(
        modifier =
            Modifier
                .then(modifier)
                .height(
                    UiConstants.calculateNumberInputHeight(),
                )
                .aspectRatio(2.5f),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NumberInputButton(
                value = value,
                onValueChange = {
                    if (checkFloatValue(
                            value = it,
                            minValue = minValue,
                            maxValue = maxValue,
                        )
                    ) {
                        textFieldValue = it.toString()
                        onValueChange(it)
                    }
                },
                step = -1,
                iconVector = Icons.Rounded.KeyboardArrowDown,
                iconDescription = "Decrement",
                enabled = !isInput,
            )

            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        isInput = it.isFocused
                        if (!it.isFocused) {
                            emitValue()
                        }
                    }
                ,
                value = textFieldValue,
                onValueChange = {
                    if (checkTextFieldValue(
                            value = it,
                            minValue = minValue,
                            maxValue = maxValue,
                        )
                    ) {
                        textFieldValue = it
                    }
                },
                textStyle =
                    TextStyle(
                        fontSize = UiConstants.defaultFontSize,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            emitValue()
                        },
                    ),
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.primary
                ),
            )

            NumberInputButton(
                value = value,
                onValueChange = {
                    if (checkFloatValue(
                            value = it,
                            minValue = minValue,
                            maxValue = maxValue,
                        )
                    ) {
                        textFieldValue = it.toString()
                        onValueChange(it)
                    }
                },
                step = 1,
                iconVector = Icons.Rounded.KeyboardArrowUp,
                iconDescription = "Increment",
                enabled = !isInput,
            )
        }
    }
}

private fun checkFloatValue(
    value: Float,
    maxValue: Int,
    minValue: Int,
) = value <= maxValue && value >= minValue

private fun checkTextFieldValue(
    value: String,
    maxValue: Int,
    minValue: Int,
): Boolean {
    val valueAsFloat = value.toFloatOrNull()
    if (
        value.contains(Regex("\\.{2,}")) ||
        value.contains(Regex("\\s")) ||
        (value.split(".").getOrNull(1)?.length ?: Int.MIN_VALUE) > 1
        || ( (valueAsFloat != null) && (!checkFloatValue(valueAsFloat, maxValue, minValue)) )
    ) {
        return false
    }

    return true
}

@Composable
private fun NumberInputButton(
    value: Float,
    onValueChange: (Float) -> Unit,
    step: Int,
    iconVector: ImageVector,
    iconDescription: String,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressing by remember { mutableStateOf(false) }
    var pressInteraction: PressInteraction.Press? by remember { mutableStateOf(null) }

    LaunchedEffect(isPressing) {
        if (isPressing) {
            val press = PressInteraction.Press(Offset.Zero)
            pressInteraction = press
            interactionSource.emit(press)

            var changeMagnitude = step
            while (isPressing && enabled) {
                onValueChange(value + changeMagnitude)
                delay(200)
                changeMagnitude += step
            }
        } else {
            pressInteraction?.let {
                interactionSource.emit(PressInteraction.Release(it))
                pressInteraction = null
            }
        }
    }

    Box(
        modifier =
            Modifier
                .clip(CircleShape)
                .indication(
                    interactionSource = interactionSource,
                    indication = if (enabled) ripple(bounded = false) else null,
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
        Icon(
            imageVector = iconVector,
            contentDescription = iconDescription,
            tint = if (enabled) {
                LocalContentColor.current
            } else LocalContentColor.current.copy(alpha = 0.4f)
        )
    }
}
