package com.mr3y.ludi.ui.screens.onboarding

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.mr3y.ludi.R
import com.mr3y.ludi.core.model.GameGenre
import com.mr3y.ludi.core.model.Result
import com.mr3y.ludi.ui.components.AnimatedNoInternetBanner
import com.mr3y.ludi.ui.components.LudiErrorBox
import com.mr3y.ludi.ui.presenter.connectivityState
import com.mr3y.ludi.ui.presenter.model.ConnectionState
import com.mr3y.ludi.ui.presenter.model.ResourceWrapper
import com.mr3y.ludi.ui.preview.LudiPreview
import com.mr3y.ludi.ui.theme.LudiTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenresPage(
    allGenres: Result<ResourceWrapper<Set<GameGenre>>, Throwable>,
    selectedGenres: Set<GameGenre>,
    onSelectingGenre: (GameGenre) -> Unit,
    onUnselectingGenre: (GameGenre) -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        val connectionState by connectivityState()
        val isInternetConnectionNotAvailable by remember {
            derivedStateOf { connectionState != ConnectionState.Available }
        }
        val label = stringResource(R.string.on_boarding_genres_page_title)
        val secondaryText = stringResource(R.string.on_boarding_secondary_text)
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.clearAndSetSemantics {
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
        AnimatedNoInternetBanner(visible = isInternetConnectionNotAvailable, modifier = Modifier.padding(vertical = 8.dp))
        val context = LocalContext.current
        when (allGenres) {
            is Result.Success -> {
                if (allGenres.data is ResourceWrapper.ActualResource) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                isTraversalGroup = true
                            },
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allGenres.data.resource.forEach { genre ->
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
                                        predicate = genre in selectedGenres,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    )
                                    .selectable(
                                        selected = genre in selectedGenres,
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        role = Role.Checkbox,
                                        onClick = {
                                            if (genre in selectedGenres) {
                                                onUnselectingGenre(genre)
                                            } else {
                                                onSelectingGenre(genre)
                                            }
                                        }
                                    )
                                    .animateContentSize()
                                    .padding(8.dp)
                                    .clearAndSetSemantics {
                                        val stateDesc = if (genre in selectedGenres) R.string.genres_page_genre_on_state_desc else R.string.genres_page_genre_off_state_desc
                                        stateDescription = context.getString(stateDesc, genre.name)
                                        selected = genre in selectedGenres
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
                                if (genre in selectedGenres) {
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
                } else {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is Result.Error -> {
                LudiErrorBox(modifier = Modifier.fillMaxWidth())
            }
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

@LudiPreview
@Composable
fun GenresPagePreview() {
    LudiTheme {
        GenresPage(
            allGenres = PreviewAllGenres,
            selectedGenres = PreviewSelectedGenres,
            onSelectingGenre = {},
            onUnselectingGenre = {},
            modifier = Modifier.padding(8.dp)
        )
    }
}
