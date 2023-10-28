package com.mr3y.ludi.shared.ui.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

@Composable
fun rememberParallaxAlignment(
    lazyListState: LazyListState,
    key: Any?
): Alignment {
    return remember(lazyListState) {
        ParallaxAlignment(
            horizontalBias = {
                if (key == null) {
                    return@ParallaxAlignment 0f
                }

                // Read the LazyListState layout info
                val layoutInfo = lazyListState.layoutInfo
                // Find the layout info of this item
                val itemInfo = layoutInfo.visibleItemsInfo.first { it.key == key }

                val adjustedOffset = itemInfo.offset - layoutInfo.viewportStartOffset
                (adjustedOffset / itemInfo.size.toFloat()).coerceIn(-1f, 1f)
            }
        )
    }
}

@Stable
class ParallaxAlignment(
    private val horizontalBias: () -> Float = { 0f },
    private val verticalBias: () -> Float = { 0f }
) : Alignment {
    override fun align(
        size: IntSize,
        space: IntSize,
        layoutDirection: LayoutDirection
    ): IntOffset {
        // Convert to Px first and only round at the end, to avoid rounding twice while calculating
        // the new positions
        val centerX = (space.width - size.width).toFloat() / 2f
        val centerY = (space.height - size.height).toFloat() / 2f
        val resolvedHorizontalBias = if (layoutDirection == LayoutDirection.Ltr) {
            horizontalBias()
        } else {
            -1 * horizontalBias()
        }

        val x = centerX * (1 + resolvedHorizontalBias)
        val y = centerY * (1 + verticalBias())
        return IntOffset(x.roundToInt(), y.roundToInt())
    }
}
