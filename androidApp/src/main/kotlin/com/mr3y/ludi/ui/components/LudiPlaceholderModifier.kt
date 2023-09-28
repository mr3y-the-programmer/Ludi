package com.mr3y.ludi.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import io.github.fornewid.placeholder.foundation.PlaceholderHighlight
import io.github.fornewid.placeholder.material3.placeholder
import io.github.fornewid.placeholder.material3.shimmer

fun Modifier.defaultPlaceholder(
    isVisible: Boolean,
    highlight: PlaceholderHighlight? = null,
    color: Color? = null
): Modifier = composed {
    placeholder(
        visible = isVisible,
        highlight = highlight ?: PlaceholderHighlight.shimmer(),
        color = color ?: MaterialTheme.colorScheme.surfaceVariant
    )
}
