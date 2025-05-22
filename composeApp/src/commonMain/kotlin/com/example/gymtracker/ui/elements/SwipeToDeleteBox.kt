package com.example.gymtracker.ui.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.gymtracker.ui.UiConstants
import kotlinx.coroutines.delay

private const val ICON_SIZE_COEF = 0.45f

@Composable
fun SwipeToDeleteBox(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    doBeforeAnimation: () -> Unit = {},
    initialVisibility: Boolean = false,
    content:
        @Composable()
        (RowScope.() -> Unit),
) {
    var isVisible by rememberSaveable { mutableStateOf(initialVisibility) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    val dismissState =
        rememberSwipeToDismissBoxState(
            confirmValueChange = { dismissValue ->
                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                    isVisible = false
                }
                true
            },
        )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        SwipeToDismissBox(
            modifier = modifier,
            state = dismissState,
            enableDismissFromStartToEnd = false,
            backgroundContent = {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .background(Color.Red),
                        contentAlignment = Alignment.CenterEnd,
                    ) {
                        Icon(
                            modifier =
                                Modifier
                                    .align(Alignment.Center)
                                    .fillMaxSize(ICON_SIZE_COEF),
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White,
                        )
                    }
                }
            },
        ) {
            content()
        }
    }

    LaunchedEffect(isVisible) {
        if (!isVisible) {
            doBeforeAnimation()
            delay(UiConstants.ANIMATION_DEFAULT_DURATION_MILLIS)
            onDelete()
            isVisible = false
        }
    }
}
