package com.example.gymtracker.ui.elements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gymtracker.ui.UiConstants

@Composable
fun AdditionalTopBar(
    isTopBarExpanded: Boolean,
    content: @Composable() (RowScope.() -> Unit),
) {
    AnimatedContent(
        targetState = isTopBarExpanded,
        transitionSpec = {
            (
                    (slideInVertically { -it } + fadeIn())
                        .togetherWith(
                            slideOutVertically { -it } + fadeOut()
                        )
                    )
                .using(
                    SizeTransform(clip = false)
                )
        },
        label = "ElapsedTimeBarAnimation",
    ) { expanded ->
        if (expanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            start = UiConstants.topAppBarHorizontalPadding,
                            end = UiConstants.topAppBarHorizontalPadding,
                        )
                        .height(UiConstants.WeekdaySwitcherFullHeight),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content,
                )
            }
        }
    }
}