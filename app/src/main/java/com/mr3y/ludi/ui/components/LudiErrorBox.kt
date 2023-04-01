package com.mr3y.ludi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mr3y.ludi.ui.theme.LudiTheme

@Composable
fun LudiErrorBox(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(color = MaterialTheme.colorScheme.surface)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Filled.Error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = "Unexpected Error happened. try to refresh, and see if the problem persists.",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }

}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = "id:pixel_6")
@Composable
fun LudiErrorBoxPreview() {
    LudiTheme {
        LudiErrorBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(445.dp)
        )
    }
}