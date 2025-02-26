package com.example.gymtracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

private const val ICON_SIZE_COEF = 0.45f

@Composable
fun SwipeToDeleteBox(
    onDelete: () -> Unit,
    snackbarHostState: SnackbarHostState,
    content:
        @Composable()
        (RowScope.() -> Unit),
) {
    val coroutineScope = rememberCoroutineScope()
    val dismissState =
        rememberSwipeToDismissBoxState(
            confirmValueChange = { dismissValue ->
                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Упражнение удалено",
                            actionLabel = "Восстановить",
                            duration = SnackbarDuration.Short,
                        )
                    }
                }
                true
            },
        )

    SwipeToDismissBox(
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
