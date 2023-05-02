package com.mr3y.ludi.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.placeholder

fun Modifier.defaultPlaceholder(isVisible: Boolean): Modifier = composed {
    placeholder(
        visible = isVisible,
        highlight = PlaceholderHighlight.fade(
            highlightColor = MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.15f
            )
        )
    )
}
