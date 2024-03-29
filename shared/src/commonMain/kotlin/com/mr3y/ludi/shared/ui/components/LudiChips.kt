package com.mr3y.ludi.shared.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LudiFilterChip(
    selected: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val strings = LocalStrings.current
    FilterChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier.semantics(mergeDescendants = true) {
            this.selected = selected
            stateDescription = if (selected) strings.filter_chip_on_state_desc(label) else strings.filter_chip_off_state_desc(label)
        },
        shape = RoundedCornerShape(50),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.clearAndSetSemantics { }
                )
            }
        } else {
            null
        }
    )
}

@Composable
fun LudiSuggestionChip(
    label: String,
    modifier: Modifier = Modifier,
    labelColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    SuggestionChip(
        onClick = { },
        modifier = modifier.wrapContentHeight().height(32.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.dp, Color.Transparent),
        colors = SuggestionChipDefaults.suggestionChipColors(
            labelColor = labelColor,
            containerColor = containerColor
        ),
        label = {
            Text(
                text = label,
                textAlign = TextAlign.Center
            )
        }
    )
}
