package com.mr3y.ludi.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A layout that places as many children as possible in exactly one single line/row until all children are placed
 * or until there is no room for another child in which case you can optionally display an overflow indicator to express
 * that children cannot fit within the available width.
 */
@Composable
fun SingleRowLayout(
    modifier: Modifier = Modifier,
    spacing: Dp = 8.dp,
    overflowIndicator: (@Composable (remainingChildrenCount: Int) -> Unit)? = null,
    overflowMinWidthRatio: Float = 0.2f,
    content: @Composable () -> Unit,
) {
    SubcomposeLayout(modifier = modifier) {constraints ->
        val measurables = subcompose(1) { content() }
        val childrenWidths = measurables.map { it.maxIntrinsicWidth(constraints.maxHeight) }
        val unOccupiedWidth = constraints.maxWidth - (childrenWidths.sum() + (measurables.size - 1) * spacing.roundToPx())
        var overflowPlaceable: Placeable? = null
        val placeables = when {
            unOccupiedWidth >= 0 -> {
                // there is no overflow & all children are fitting within the available width
                measurables.map {
                    it.measure(
                        constraints.copy(
                            minWidth = it.minIntrinsicWidth(constraints.maxHeight), maxWidth = it.maxIntrinsicWidth(constraints.maxHeight),
                        )
                    )
                }
            }
            else -> {
                // can't fit all children within the available width
                var numOfFittingChildren = 0
                var occupiedWidth = 0
                var i = 0
                while (occupiedWidth + childrenWidths[i] + spacing.roundToPx() < constraints.maxWidth) {
                    occupiedWidth += childrenWidths[i] + spacing.roundToPx()
                    i++
                    numOfFittingChildren++
                }
                val overflowMeasurable = if (overflowIndicator != null)
                    subcompose(2) {
                        overflowIndicator.invoke(measurables.size - numOfFittingChildren)
                    }.single()
                else
                    null
                val overflowPlaceableWidth = constraints.maxWidth - occupiedWidth - spacing.roundToPx()
                overflowPlaceable = overflowMeasurable?.measure(
                    constraints.copy(
                        maxWidth = overflowPlaceableWidth,
                        minWidth = 0,
                        minHeight = 0,
                        maxHeight = overflowMeasurable.maxIntrinsicHeight(overflowPlaceableWidth).coerceAtMost(constraints.maxHeight)
                    )
                )
                measurables.take(numOfFittingChildren).map {
                    it.measure(
                        constraints.copy(
                            minWidth = it.minIntrinsicWidth(constraints.maxHeight),
                            maxWidth = it.maxIntrinsicWidth(constraints.maxHeight),
                        )
                    )
                }
            }
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            var x = 0
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(x = x, y = 0)
                x += placeable.measuredWidth + if (index != placeables.lastIndex) spacing.roundToPx() else 0
            }
            overflowPlaceable?.placeRelative(x = x + spacing.roundToPx(), y = 0)
        }
    }
}