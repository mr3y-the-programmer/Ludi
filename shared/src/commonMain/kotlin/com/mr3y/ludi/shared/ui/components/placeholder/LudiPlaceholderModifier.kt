package com.mr3y.ludi.shared.ui.components.placeholder

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color

fun Modifier.defaultPlaceholder(
    isVisible: Boolean,
    highlight: PlaceholderHighlight? = null,
    color: Color? = null
): Modifier = composed {
    placeholder(
        visible = isVisible,
        highlight = highlight ?: PlaceholderHighlight.shimmer(),
        color = color ?: MaterialTheme.colorScheme.surfaceVariant,
        shape = null
    )
}
