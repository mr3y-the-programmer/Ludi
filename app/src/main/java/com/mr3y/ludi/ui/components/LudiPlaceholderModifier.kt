package com.mr3y.ludi.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

fun Modifier.defaultPlaceholder(
    isVisible: Boolean,
    highlight: PlaceholderHighlight? = null
): Modifier = composed {
    placeholder(
        visible = isVisible,
        highlight = highlight ?: PlaceholderHighlight.fade()
    )
}
