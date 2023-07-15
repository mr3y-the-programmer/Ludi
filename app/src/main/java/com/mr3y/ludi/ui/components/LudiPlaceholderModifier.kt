package com.mr3y.ludi.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

fun Modifier.defaultPlaceholder(
    isVisible: Boolean,
    highlight: PlaceholderHighlight? = null,
    color: Color? = null
): Modifier = composed {
    placeholder(
        visible = isVisible,
        highlight = highlight ?: PlaceholderHighlight.fade(),
        color = color ?: Color.Unspecified
    )
}
