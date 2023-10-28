package com.mr3y.ludi.shared.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings

@Composable
fun RefreshIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.inverseSurface
) {
    val strings = LocalStrings.current
    IconButton(
        onClick = onClick,
        modifier = modifier
            .requiredSize(48.dp)
            .semantics {
                contentDescription = strings.click_to_refresh
            }
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            tint = tint
        )
    }
}
