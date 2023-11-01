package com.mr3y.ludi.shared.ui.screens.onboarding

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.lyricist.LocalStrings
import com.mr3y.ludi.shared.core.model.GameGenre
import com.mr3y.ludi.shared.core.model.Result
import com.mr3y.ludi.shared.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.shared.ui.components.LudiErrorBox
import com.mr3y.ludi.shared.ui.components.RefreshIconButton
import com.mr3y.ludi.shared.ui.resources.isDesktopPlatform

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenresPage(
    allGenres: Result<Set<GameGenre>, Throwable>,
    selectedGenres: Set<GameGenre>,
    onSelectingGenre: (GameGenre) -> Unit,
    onUnselectingGenre: (GameGenre) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        val strings = LocalStrings.current
        val label = strings.on_boarding_genres_page_title
        val secondaryText = strings.on_boarding_secondary_text
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth().clearAndSetSemantics {
                contentDescription = "$label\n$secondaryText"
            }
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = secondaryText,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        AnimatedNoInternetBanner(modifier = Modifier.padding(vertical = 8.dp))
        when (allGenres) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Result.Success -> {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            isTraversalGroup = true
                        },
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allGenres.data.forEach { genre ->
                        val isSelected = genre.id in selectedGenres.map { it.id }
                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .border(
                                    1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .conditionalBackground(
                                    predicate = isSelected,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                )
                                .selectable(
                                    selected = isSelected,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    role = Role.Checkbox,
                                    onClick = {
                                        if (isSelected) {
                                            onUnselectingGenre(genre)
                                        } else {
                                            onSelectingGenre(genre)
                                        }
                                    }
                                )
                                .animateContentSize()
                                .padding(8.dp)
                                .clearAndSetSemantics {
                                    val stateDesc = if (isSelected) strings.genres_page_genre_on_state_desc(genre.name) else strings.genres_page_genre_off_state_desc(genre.name)
                                    stateDescription = stateDesc
                                    selected = isSelected
                                    role = Role.Checkbox
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "\n",
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = genre.name,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.width(IntrinsicSize.Min),
                                textAlign = TextAlign.Center
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.padding(8.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            is Result.Error -> {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
        }
        if (allGenres !is Result.Loading && isDesktopPlatform()) {
            RefreshIconButton(
                onClick = onRefresh,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

private fun Modifier.conditionalBackground(color: Color, predicate: Boolean): Modifier {
    return if (predicate) {
        background(color)
    } else {
        this
    }
}
