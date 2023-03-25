package com.mr3y.ludi.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LudiSectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.h4,
        modifier = modifier,
        textAlign = TextAlign.Start
    )
}