package com.example.gymtracker.ui.elements

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import kotlinx.coroutines.flow.collectLatest

private fun Density.spToDp(sp: TextUnit) = if (sp.isSpecified) sp.toDp() else Dp.Unspecified

@Composable
private fun TextUnit.toDp() = LocalDensity.current.spToDp(this)

@Stable
private fun Modifier.fadingEdge(brush: Brush) =
    this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.DstIn)
        }

/**
 * A composable function that allows users to select an item from a list using a scrollable list with a text field for editing.
 *
 * @param initialValue The initial value to be selected in the list.
 * @param values The list of items.
 * @param modifier Modifier for customizing the appearance of the `ListPicker`.
 * @param wrapSelectorWheel Boolean flag indicating whether the list should wrap around like a selector wheel.
 * @param format A lambda function that formats an item into a string for display.
 * @param onValueChange A callback function that is invoked when the selected item changes.
 * @param onIsErrorChange A callback function that is invoked when the isError changes.
 * @param parse A lambda function that parses a string into an item.
 * @param enableEdition Boolean flag indicating whether the user can edit the selected item using a text field.
 * @param outOfBoundsPageCount The number of pages to display on either side of the selected item.
 * @param textStyle The text style for the displayed items.
 * @param verticalPadding The vertical padding between items.
 * @param dividerColor The color of the horizontal dividers.
 * @param dividerThickness The thickness of the horizontal dividers.
 *
 * @author Reda El Madini - For support, contact gladiatorkilo@gmail.com
 */
@Composable
fun ListPicker(
    initialValue: Int,
    values: List<Int>,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    wrapSelectorWheel: Boolean = false,
    outOfBoundsPageCount: Int = 1,
    textStyle: TextStyle = LocalTextStyle.current,
    verticalPadding: Dp = 16.dp,
    dividerColor: Color = MaterialTheme.colorScheme.outline,
    dividerThickness: Dp = 1.dp,
) {
    val listSize = values.size
    val coercedOutOfBoundsPageCount = outOfBoundsPageCount.coerceIn(0..listSize / 2)
    val visibleItemsCount = 1 + coercedOutOfBoundsPageCount * 2
    val iteration =
        if (wrapSelectorWheel) {
            remember(key1 = coercedOutOfBoundsPageCount, key2 = listSize) {
                (Int.MAX_VALUE - 2 * coercedOutOfBoundsPageCount) / listSize
            }
        } else {
            1
        }
    val intervals =
        remember(key1 = coercedOutOfBoundsPageCount, key2 = iteration, key3 = listSize) {
            listOf(
                0,
                coercedOutOfBoundsPageCount,
                coercedOutOfBoundsPageCount + iteration * listSize,
                coercedOutOfBoundsPageCount + iteration * listSize + coercedOutOfBoundsPageCount,
            )
        }
    val scrollOfItemIndex = { it: Int ->
        it + (listSize * (iteration / 2))
    }
    val scrollOfItem = { item: Int ->
        values.indexOf(item)
            .takeIf { it != -1 }
            ?.let { index -> scrollOfItemIndex(index) }
    }
    val lazyListState =
        rememberLazyListState(
            initialFirstVisibleItemIndex =
                remember(
                    key1 = initialValue,
                    key2 = listSize,
                    key3 = iteration,
                ) {
                    scrollOfItem(initialValue) ?: 0
                },
        )
    LaunchedEffect(key1 = values) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }.collectLatest {
            onValueChange(values[it % listSize])
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val itemHeight = textStyle.lineHeight.toDp() + verticalPadding * 2F
        LazyRow(
            state = lazyListState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState),
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxHeight()
                    .width(itemHeight * visibleItemsCount)
                    .fadingEdge(
                        brush =
                            remember {
                                Brush.horizontalGradient(
                                    0F to Color.Transparent,
                                    0.5F to Color.Black,
                                    1F to Color.Transparent,
                                )
                            },
                    ),
        ) {
            items(
                count = intervals.last(),
                key = { it },
            ) { index ->
                val textModifier = Modifier.padding(horizontal = verticalPadding)
                when (index) {
                    in intervals[0]..<intervals[1] ->
                        Text(
                            text = if (wrapSelectorWheel) values[(index - coercedOutOfBoundsPageCount + listSize) % listSize].toString() else "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = textStyle,
                            modifier = textModifier,
                        )

                    in intervals[1]..<intervals[2] -> {
                        Text(
                            text = values[(index - coercedOutOfBoundsPageCount) % listSize].toString(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = textStyle,
                            modifier = textModifier,
                        )
                    }

                    in intervals[2]..<intervals[3] ->
                        Text(
                            text = if (wrapSelectorWheel) values[(index - coercedOutOfBoundsPageCount) % listSize].toString() else "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = textStyle,
                            modifier = textModifier,
                        )
                }
            }
        }

        VerticalDivider(
            modifier = Modifier.offset(x = itemHeight * coercedOutOfBoundsPageCount - dividerThickness / 2),
            thickness = dividerThickness,
            color = dividerColor,
        )

        VerticalDivider(
            modifier = Modifier.offset(x = itemHeight * (coercedOutOfBoundsPageCount + 1) - dividerThickness / 2),
            thickness = dividerThickness,
            color = dividerColor,
        )
    }
}
